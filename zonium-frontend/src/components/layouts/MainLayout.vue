<template>
  <div class="layout-container">
    <aside class="sidebar">
      <div class="logo">
        <h2>zonium</h2>
      </div>
      <nav>
        <router-link to="/main">Главная</router-link>
        <router-link to="/proxies">Мои прокси</router-link>
        <router-link to="/profile">Мой профиль</router-link>
      </nav>
    </aside>

    <main class="main-content">
      <router-view/>
    </main>
  </div>
</template>

<style scoped>
.layout-container {
  display: flex;
  height: 100vh;
}

.sidebar {
  width: 240px;
  background-color: #f5f5f5;
  padding: 20px;
  box-sizing: border-box;
  border-right: 1px solid #ddd;
  display: flex;
  flex-direction: column;
}

.logo {
  margin-bottom: 15px;
  font-weight: bold;
  font-size: 1.5rem;
  text-align: center;
}

.sidebar nav {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.sidebar nav a {
  color: #333;
  text-decoration: none;
  padding: 8px 12px;
  border-radius: 6px;
}

.sidebar nav a.router-link-active {
  background-color: #42b983;
  color: white;
}

.main-content {
  flex-grow: 1; /* занимает всё оставшееся пространство */
  padding: 30px;
  overflow-y: auto;
}

</style>
<script setup lang="ts">
</script>