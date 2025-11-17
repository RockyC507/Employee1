<template>
  <div id="gk-shop">
    <!-- ===== HEADER 區域 ===== -->
    <header class="main-header">
      <!-- 標題 -->
      <div class="header-item gk-title">
        <h1>GK 公仔專賣店</h1>
      </div>
      
      <!-- 導覽列 -->
      <nav class="main-nav">
        <div class="nav-links">
          <!-- 切換不同頁面（使用 currentPage 控制顯示） -->
          <span class="nav-item" :class="{ active: currentPage === 'products' }" @click="currentPage = 'products'">商品列表</span>
          <span class="nav-item" :class="{ active: currentPage === 'orders' }" @click="currentPage = 'orders'">我的訂單</span>
          <span class="nav-item" :class="{ active: currentPage === 'wallet' }" @click="currentPage = 'wallet'">我的錢包</span>
        </div>

        <!-- 登入/登出 按鈕 -->
        <div class="nav-auth">
          <span class="nav-item login-nav" v-if="!isLoggedIn" @click="currentPage = 'login'">登入</span>
          <span class="nav-item logout-nav" v-else @click="logout">登出</span>
        </div>
      </nav>
    </header>

    <!-- ===== MAIN 主要內容區 ===== -->
    <main class="main-content-area">
      
      <!-- 側邊欄 (分類與快速選單) -->
      <aside class="sidebar">
        <!-- 商品分類下拉選單 -->
        <div class="filter-dropdown">
          <select class="category-select" v-model="selectedCategory">
            <option value="">全部類別</option>
            <option value="gundam">鋼彈系列</option>
            <option value="onepiece">ONE PIECE</option>
            <option value="naruto">火影忍者</option>
            <option value="pokemon">寶可夢</option>
          </select>
        </div>

        <!-- 篩選條件 -->
        <div class="filter-list">
          <h4>篩選條件</h4>
          <p>價格: $1000-2000</p>
          <p>價格: $2000-3000</p>
          <p>價格: $3000以上</p>
          <p>-----</p>
          <p>熱門商品</p>
          <p>最新上架</p>
        </div>

        <!-- 額外資訊區塊 -->
        <div class="extra-block">
          <h4>會員資訊</h4>
          <p v-if="isLoggedIn">歡迎回來！</p>
          <p v-else>請先登入</p>
        </div>

        <!-- 快速連結 -->
        <div class="extra-block">
          <h4>快速連結</h4>
          <p @click="currentPage = 'orders'" style="cursor: pointer;">📦 訂單查詢</p>
        </div>
      </aside>

      <!-- ===== 商品列表頁 ===== -->
      <section class="product-display" v-if="currentPage === 'products'">
        <div class="product-card" v-for="product in filteredProducts" :key="product.id">
          <div class="product-image">
            <span>{{ product.name }}</span>
          </div>
          <div class="product-info">
            <h3>{{ product.name }}</h3>
            <p class="price">NT$ {{ product.price.toLocaleString() }}</p>
            <p class="market-price">市價: NT$ {{ product.marketPrice.toLocaleString() }}</p>
            <!-- 顯示庫存狀態 -->
            <span class="stock-badge" v-if="product.stock > 0">庫存: {{ product.stock }}</span>
            <span class="stock-badge out-of-stock" v-else>已售完</span>
            <button class="buy-btn" @click="addToCart(product)">加入購物車</button>
          </div>
        </div>
      </section>

      <!-- ===== 投資追蹤頁（收藏品價值變化） ===== -->
      <section class="content-section" v-if="currentPage === 'investment'">
        <h2 class="section-title">我的投資組合</h2>
        
        <!-- 總投資統計 -->
        <div class="stats-container">
          <div class="stat-box">
            <h4>總投資金額</h4>
            <p class="stat-value">NT$ {{ totalInvestment.toLocaleString() }}</p>
          </div>
          <div class="stat-box">
            <h4>當前總價值</h4>
            <p class="stat-value">NT$ {{ currentValue.toLocaleString() }}</p>
          </div>
          <div class="stat-box highlight-stat">
            <h4>總報酬率</h4>
            <p class="stat-value" :class="totalROI >= 0 ? 'positive' : 'negative'">
              {{ totalROI >= 0 ? '+' : '' }}{{ totalROI.toFixed(2) }}%
            </p>
          </div>
        </div>

        <!-- 個別收藏品列表 -->
        <div class="investment-list">
          <div class="investment-item" v-for="item in collection" :key="item.id">
            <div class="item-header">
              <h3>{{ item.name }}</h3>
              <span class="roi-tag" :class="item.roi >= 0 ? 'roi-positive' : 'roi-negative'">
                {{ item.roi >= 0 ? '+' : '' }}{{ item.roi.toFixed(2) }}%
              </span>
            </div>
            <div class="item-details">
              <div class="detail-row">
                <span>購入價格:</span>
                <span>NT$ {{ item.purchasePrice.toLocaleString() }}</span>
              </div>
              <div class="detail-row">
                <span>當前市價:</span>
                <span class="highlight-value">NT$ {{ item.currentPrice.toLocaleString() }}</span>
              </div>
              <div class="detail-row">
                <span>購入日期:</span>
                <span>{{ item.purchaseDate }}</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- ===== 訂單頁 ===== -->
      <section class="content-section" v-if="currentPage === 'orders'">
        <h2 class="section-title">我的訂單</h2>
        <div class="order-list">
          <div class="order-item" v-for="order in orders" :key="order.id">
            <div class="order-header">
              <div>
                <h3>訂單編號: {{ order.id }}</h3>
                <p class="order-date">{{ order.date }}</p>
              </div>
              <span class="order-status" :class="getStatusClass(order.status)">
                {{ order.status }}
              </span>
            </div>
            <div class="order-total">
              總金額: <strong>NT$ {{ order.totalAmount.toLocaleString() }}</strong>
            </div>
          </div>
        </div>
      </section>

      <!-- ===== 錢包頁 ===== -->
      <section class="content-section" v-if="currentPage === 'wallet'">
        <h2 class="section-title">我的錢包</h2>
        <div class="wallet-container">
          <!-- 餘額顯示 -->
          <div class="wallet-balance">
            <h3>當前餘額</h3>
            <p class="balance-amount">NT$ {{ walletBalance.toLocaleString() }}</p>
          </div>

          <!-- 儲值/查看交易紀錄 -->
          <div class="wallet-actions">
            <button class="action-btn" @click="showTopUp = true">儲值</button>
            <button class="action-btn secondary" @click="showTransactions = !showTransactions">交易記錄</button>
          </div>
          
          <!-- 儲值表單 -->
          <div v-if="showTopUp" class="topup-form">
            <h4>儲值金額</h4>
            <input type="number" v-model="topUpAmount" placeholder="請輸入金額">
            <button class="buy-btn" @click="topUp">確認儲值</button>
            <button class="cancel-btn" @click="showTopUp = false">取消</button>
          </div>

          <!-- 交易紀錄 -->
          <div v-if="showTransactions" class="transaction-list">
            <h4>交易記錄</h4>
            <div class="transaction-item" v-for="trans in transactions" :key="trans.id">
              <span>{{ trans.type }}</span>
              <span :class="trans.amount > 0 ? 'positive' : 'negative'">
                {{ trans.amount > 0 ? '+' : '' }}NT$ {{ Math.abs(trans.amount).toLocaleString() }}
              </span>
              <span class="trans-date">{{ trans.date }}</span>
            </div>
          </div>
        </div>
      </section>

      <!-- ===== 登入頁 ===== -->
      <section class="content-section" v-if="currentPage === 'login'">
        <Login @login-success="handleLoginSuccess" />
      </section>

    </main>

    <!-- ===== FOOTER ===== -->
    <footer class="main-footer">
      <div class="footer-links">
        <a href="#">關於我們</a> | 
        <a href="#">聯絡我們</a> | 
        <a href="#">訂單查詢</a> | 
        <a href="#">售後服務</a> |
        <a href="#">隱私權政策</a>
      </div>
      <div class="footer-info">
        地址: 台北市XX區XX路XX號 | 電話: 02-1234-5678 | 營業時間: 週一至週日 10:00-22:00
      </div>
      <div class="footer-info">
        © 2025 GK買賣交易平台 版權所有
      </div>
    </footer>

  </div>
</template>

<script setup>
/* ===== Vue 3 Composition API 區塊 ===== */
import { ref, computed } from 'vue';
import Login from './components/Login.vue';

/* 當前頁面狀態控制 */
const currentPage = ref('products'); // 預設顯示商品頁
const isLoggedIn = ref(false); // 是否登入狀態
const selectedCategory = ref(''); // 篩選用的商品分類
const showTopUp = ref(false); // 是否顯示儲值介面
const showTransactions = ref(false); // 是否顯示交易紀錄
const topUpAmount = ref(0); // 儲值金額
const walletBalance = ref(15000); // 使用者錢包餘額

/* 商品資料 */
const products = ref([
  { id: 1, name: '鋼彈 RX-78-2', price: 2500, marketPrice: 3200, stock: 5, category: 'gundam'},
  { id: 2, name: 'ONE PIECE 魯夫', price: 1800, marketPrice: 1650, stock: 3, category: 'onepiece' },
  { id: 3, name: '火影忍者 鳴人', price: 2200, marketPrice: 2800, stock: 8, category: 'naruto' },
  { id: 4, name: '寶可夢 皮卡丘', price: 1500, marketPrice: 2000, stock: 12, category: 'pokemon' },
  { id: 5, name: '鬼滅之刃 炭治郎', price: 2000, marketPrice: 2400, stock: 0, category: 'other' },
  { id: 6, name: '七龍珠 艾連', price: 2300, marketPrice: 1900, stock: 6, category: 'other' },
  { id: 7, name: '死神 艾連', price: 2300, marketPrice: 1900, stock: 6, category: 'other' },
  { id: 8, name: '遊戲王 艾連', price: 2300, marketPrice: 1900, stock: 6, category: 'other' },
  { id: 9, name: '名偵探柯南 艾連', price: 2300, marketPrice: 1900, stock: 6, category: 'other' }
]);

/* 收藏品投資組合（模擬投資回報） */
const collection = ref([
  { id: 1, name: '鋼彈 RX-78-2', purchasePrice: 2500, currentPrice: 3200, purchaseDate: '2024-09-15', roi: 28 },
  { id: 2, name: 'ONE PIECE 魯夫', purchasePrice: 1800, currentPrice: 1650, purchaseDate: '2024-10-01', roi: -8.33 }
]);

/* 訂單資料 */
const orders = ref([
  { id: 'ORD001', totalAmount: 4300, status: '已完成', date: '2024-09-15' },
  { id: 'ORD002', totalAmount: 2200, status: '配送中', date: '2024-10-20' },
  { id: 'ORD003', totalAmount: 1500, status: '待付款', date: '2024-11-01' }
]);

/* 錢包交易紀錄 */
const transactions = ref([
  { id: 1, type: '儲值', amount: 5000, date: '2024-10-01' },
  { id: 2, type: '購買商品', amount: -2500, date: '2024-10-05' },
  { id: 3, type: '儲值', amount: 10000, date: '2024-10-15' },
  { id: 4, type: '購買商品', amount: -1800, date: '2024-10-20' }
]);

/* 根據分類篩選商品 */
const filteredProducts = computed(() => {
  if (!selectedCategory.value) return products.value;
  return products.value.filter(p => p.category === selectedCategory.value);
});

/* 投資統計用的計算屬性 */
const totalInvestment = computed(() => collection.value.reduce((sum, item) => sum + item.purchasePrice, 0));
const currentValue = computed(() => collection.value.reduce((sum, item) => sum + item.currentPrice, 0));
const totalROI = computed(() => {
  if (totalInvestment.value === 0) return 0;
  return ((currentValue.value - totalInvestment.value) / totalInvestment.value) * 100;
});

/* 登入成功回呼 */
function handleLoginSuccess() {
  isLoggedIn.value = true;
  currentPage.value = 'products';
}

/* 登出功能 */
function logout() {
  isLoggedIn.value = false;
  currentPage.value = 'products';
}

/* 加入購物車（示範） */
function addToCart(product) {
  alert(`已將 ${product.name} 加入購物車（需串接後端）`);
}

/* 錢包儲值邏輯 */
function topUp() {
  if (topUpAmount.value > 0) {
    walletBalance.value += parseInt(topUpAmount.value);
    // 新增一筆交易紀錄
    transactions.value.unshift({
      id: Date.now(),
      type: '儲值',
      amount: parseInt(topUpAmount.value),
      date: new Date().toISOString().split('T')[0]
    });
    alert(`成功儲值 NT$ ${topUpAmount.value}`);
    topUpAmount.value = 0;
    showTopUp.value = false;
  }
}

/* 根據訂單狀態回傳對應樣式 class */
function getStatusClass(status) {
  const map = {
    '已完成': 'status-completed',
    '配送中': 'status-shipping',
    '待付款': 'status-pending'
  };
  return map[status] || '';
}
</script>


<style scoped>
/* 基礎樣式 */
#gk-shop {
  font-family: 'Microsoft JhengHei', Arial, sans-serif;
  margin: 0 auto;
  border: 1px solid #ccc;
  background: #fff;
  color: #333;
}

/* HEADER */
.main-header {
  border-bottom: 2px solid #000;
  padding: 15px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.gk-title {
  text-align: center;
  padding: 0;
}

.gk-title h1 {
  margin: 0;
  font-size: 1.8rem;
  font-weight: 700;
  color: #000;
}

.main-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-grow: 1;
}

.nav-links {
  display: flex;
  justify-content: center;
  flex-grow: 1;
}

.nav-item {
  padding: 8px 20px;
  margin: 5px;
  cursor: pointer;
  font-weight: 700;
  border: 1px solid transparent;
  transition: all 0.3s;
  font-size: 1rem;
}

.nav-item:hover {
  color: #cc0000;
}

.nav-item.active {
  color: #cc0000;
  border-bottom: 2px solid #cc0000;
}

.login-nav {
  background: #4CAF50;
  color: white;
  border-radius: 4px;
}

.logout-nav {
  background: #f44336;
  color: white;
  border-radius: 4px;
}

/* MAIN CONTENT */
.main-content-area {
  display: flex;
  min-height: 600px;
  padding: 20px;
  gap: 20px;
}

/* 側邊欄 */
.sidebar {
  width: 200px;
  display: flex;
  flex-direction: column;
  gap: 15px;
  flex-shrink: 0;
}

.filter-dropdown {
  border: 1px solid #000;
  padding: 0;
  height: 40px;
}

.category-select {
  width: 100%;
  height: 100%;
  padding: 5px;
  border: none;
  font-size: 0.95rem;
  font-weight: 700;
}

.filter-list, .extra-block {
  border: 1px solid #000;
  padding: 10px;
}

.filter-list h4, .extra-block h4 {
  margin: 0 0 10px 0;
  font-size: 1.1rem;
  border-bottom: 1px solid #ddd;
  padding-bottom: 5px;
  font-weight: 700;
  color: #000;
}

.filter-list p, .extra-block p {
  margin: 8px 0;
  font-size: 0.9rem;
  cursor: pointer;
}

.filter-list p:hover {
  color: #cc0000;
}

/* 商品展示 */
.product-display {
  flex-grow: 1;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.product-card {
  border: 1px solid #000;
  display: flex;
  flex-direction: column;
  transition: transform 0.3s;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.2);
}

.product-image {
  height: 150px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  font-size: 0.95rem;
  text-align: center;
  padding: 10px;
}

.product-info {
  padding: 15px;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
}

.product-info h3 {
  margin: 0 0 10px 0;
  font-size: 1.2rem;
  font-weight: 700;
  color: #000;
}

.price {
  color: #cc0000;
  font-size: 1.4rem;
  font-weight: 700;
  margin: 5px 0;
}

.market-price {
  color: #4CAF50;
  font-size: 0.9rem;
  margin: 5px 0;
  font-weight: 700;
}

.stock-badge {
  display: inline-block;
  padding: 4px 8px;
  background: #e8f5e9;
  color: #2e7d32;
  font-size: 0.85rem;
  border-radius: 3px;
  margin: 5px 0;
  font-weight: 700;
}

.stock-badge.out-of-stock {
  background: #ffebee;
  color: #c62828;
}

.buy-btn {
  margin-top: auto;
  padding: 10px;
  background: #000;
  color: white;
  border: none;
  cursor: pointer;
  font-weight: 700;
  transition: background 0.3s;
}

.buy-btn:hover {
  background: #333;
}

/* 內容區塊 */
.content-section {
  flex-grow: 1;
  padding: 20px;
  border: 1px solid #ddd;
}

.section-title {
  margin: 0 0 20px 0;
  padding-bottom: 10px;
  border-bottom: 2px solid #000;
  font-size: 1.6rem;
  font-weight: 700;
  color: #000;
}

/* 統計區 */
.stats-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 15px;
  margin-bottom: 30px;
}

.stat-box {
  border: 1px solid #000;
  padding: 20px;
  text-align: center;
}

.stat-box h4 {
  margin: 0 0 10px 0;
  font-size: 1rem;
  color: #333;
  font-weight: 700;
}

.stat-value {
  font-size: 1.8rem;
  font-weight: 700;
  margin: 0;
}

.highlight-stat {
  background: #f5f5f5;
}

.positive { color: #4CAF50; }
.negative { color: #f44336; }

/* 投資列表 */
.investment-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.investment-item {
  border: 1px solid #000;
  padding: 15px;
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ddd;
}

.item-header h3 {
  margin: 0;
  font-size: 1.2rem;
  font-weight: 700;
  color: #000;
}

.roi-tag {
  padding: 6px 12px;
  border-radius: 4px;
  font-weight: 700;
  font-size: 1.1rem;
}

.roi-positive {
  background: #e8f5e9;
  color: #2e7d32;
}

.roi-negative {
  background: #ffebee;
  color: #c62828;
}

.item-details {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.detail-row {
  display: flex;
  justify-content: space-between;
  font-weight: 700;
}

.highlight-value {
  color: #667eea;
  font-weight: 700;
}

/* 訂單列表 */
.order-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.order-item {
  border: 1px solid #000;
  padding: 15px;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.order-header h3 {
  margin: 0;
  font-size: 1.1rem;
  font-weight: 700;
  color: #000;
}

.order-date {
  color: #666;
  font-size: 0.9rem;
  margin: 5px 0 0 0;
}

.order-status {
  padding: 6px 12px;
  border-radius: 4px;
  font-weight: 700;
  font-size: 0.9rem;
}

.status-completed {
  background: #e8f5e9;
  color: #2e7d32;
}

.status-shipping {
  background: #e3f2fd;
  color: #1565c0;
}

.status-pending {
  background: #fff3e0;
  color: #e65100;
}

.order-total {
  font-size: 1.1rem;
  font-weight: 700;
}

.order-total strong {
  color: #cc0000;
  font-size: 1.3rem;
}

/* 錢包 */
.wallet-container {
  max-width: 600px;
}

.wallet-balance {
  border: 2px solid #000;
  padding: 30px;
  text-align: center;
  margin-bottom: 20px;
  background: #f9f9f9;
}

.wallet-balance h3 {
  font-weight: 700;
  color: #000;
}

.balance-amount {
  font-size: 2.5rem;
  font-weight: 700;
  color: #4CAF50;
  margin: 10px 0 0 0;
}

.wallet-actions {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.action-btn {
  flex: 1;
  padding: 12px;
  background: #000;
  color: white;
  border: none;
  cursor: pointer;
  font-weight: 700;
}

.action-btn.secondary {
  background: #666;
}

.topup-form {
  border: 1px solid #000;
  padding: 20px;
  margin-bottom: 20px;
}

.topup-form h4 {
  margin: 0 0 15px 0;
  font-weight: 700;
  color: #000;
}

.topup-form input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  margin-bottom: 10px;
  font-size: 1rem;
}

.cancel-btn {
  padding: 10px 20px;
  background: #999;
  color: white;
  border: none;
  cursor: pointer;
  margin-left: 10px;
  font-weight: 700;
}

.transaction-list {
  border: 1px solid #000;
  padding: 20px;
}

.transaction-list h4 {
  margin: 0 0 15px 0;
  border-bottom: 1px solid #ddd;
  padding-bottom: 10px;
  font-weight: 700;
  color: #000;
}

.transaction-item {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
  font-weight: 700;
}

.trans-date {
  color: #999;
  font-size: 0.9rem;
}

/* 登入 */
.login-container {
  max-width: 400px;
  margin: 50px auto;
  border: 2px solid #000;
  padding: 30px;
}

.login-container h2 {
  text-align: center;
  margin: 0 0 30px 0;
  font-weight: 700;
  color: #000;
}

.login-form {
  display: flex;
  flex-direction: column;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 700;
  color: #000;
}

.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  font-size: 1rem;
}

.login-footer {
  text-align: center;
  margin-top: 15px;
  color: #666;
}

.login-footer a {
  color: #cc0000;
  text-decoration: none;
  font-weight: 700;
}

/* FOOTER */
.main-footer {
  border-top: 2px solid #000;
  padding: 20px;
  text-align: center;
  background: #f4f4f4;
}

.footer-links {
  margin-bottom: 10px;
}

.footer-links a {
  margin: 0 10px;
  color: #333;
  text-decoration: none;
  font-weight: 700;
}

.footer-links a:hover {
  color: #cc0000;
}

.footer-info {
  margin: 5px 0;
  font-size: 0.9rem;
  color: #666;
}

/* 響應式 */

/* 平板/小筆電 (1024px) */
@media (max-width: 1024px) {
  #gk-shop {
    max-width: 100%;
    border: none;
  }
  
  .main-header {
    flex-direction: column;
  }

  .main-content-area {
    padding: 15px;
    gap: 15px;
  }

  .sidebar {
    width: 180px;
  }

  .product-display {
    grid-template-columns: repeat(2, 1fr);
    gap: 15px;
  }

  .stats-container {
    grid-template-columns: repeat(2, 1fr);
  }

  .stat-box:last-child {
    grid-column: 1 / -1;
  }

  .nav-item {
    padding: 8px 15px;
    font-size: 0.95rem;
  }

  .gk-title h1 {
    font-size: 1.7rem;
  }
}

/* 平板直式 (768px) */
@media (max-width: 768px) {
  .main-content-area {
    flex-direction: column;
  }

  .sidebar {
    width: 100%;
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 10px;
  }

  .product-display {
    grid-template-columns: repeat(2, 1fr);
    gap: 15px;
  }

  .stats-container {
    grid-template-columns: 1fr;
  }

  .nav-item {
    padding: 8px 12px;
    font-size: 0.9rem;
  }

  .main-nav {
    gap: 5px;
    width: 100%;
  }

  .gk-title h1 {
    font-size: 1.5rem;
  }
}

/* 手機版 (480px) */
@media (max-width: 480px) {
  .main-content-area {
    padding: 10px;
  }

  .sidebar {
    grid-template-columns: 1fr;
  }

  .product-display {
    grid-template-columns: 1fr;
    gap: 10px;
  }

  .wallet-actions {
    flex-direction: column;
  }

  .main-nav {
    flex-direction: column;
    align-items: stretch;
  }
  
  .nav-links {
    flex-direction: column;
    align-items: stretch;
  }
  
  .nav-auth {
    margin-top: 10px;
  }

  .nav-item {
    text-align: center;
    margin: 2px 0;
    border: 1px solid #ddd;
  }

  .gk-title h1 {
    font-size: 1.3rem;
  }

  .product-image {
    height: 120px;
  }

  .stat-value {
    font-size: 1.5rem;
  }

  .footer-links a {
    display: block;
    margin: 5px 0;
  }
}
</style>