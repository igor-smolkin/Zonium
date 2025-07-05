<template>
  <div class="container">
    <h1>Управление прокси и сессиями</h1>

    <div class="buttons">
      <button @click="showAddProxy = true">Добавить прокси</button>
      <button @click="showAddSession = true">Создать сессию</button>
      <button @click="loadProxies">Список прокси</button>
    </div>

    <!-- Модальные окна -->
    <AddProxyModal
      v-if="showAddProxy"
      @created="onProxyCreated"
      @cancel="showAddProxy = false"
    />

    <AddSessionModal
      v-if="showAddSession"
      :proxies="proxies"
      @created="onSessionCreated"
      @cancel="showAddSession = false"
    />

    <!-- Списки -->
    <ProxyList :proxies="proxies" />
    <SessionList
      :sessions="sessions"
      :proxies="proxies"
      @open-session="openSessionInNode"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

import AddProxyModal from './components/AddProxyModal.vue'
import AddSessionModal from './components/AddSessionModal.vue'
import ProxyList from './components/ProxyList.vue'
import SessionList from './components/SessionList.vue'

// Состояния
const showAddProxy = ref(false)
const showAddSession = ref(false)
const proxies = ref([])
const sessions = ref([])

// Загрузка прокси
async function loadProxies() {
  try {
    const res = await fetch('http://localhost:8080/api/proxy')
    proxies.value = await res.json()
    await loadSessions()
  } catch (e) {
    alert('Ошибка загрузки прокси')
  }
}

// Загрузка сессий
async function loadSessions() {
  try {
    const res = await fetch('http://localhost:8080/api/session')
    sessions.value = await res.json()
  } catch (e) {
    alert('Ошибка загрузки сессий')
  }
}

// Обработчик после создания прокси
async function onProxyCreated() {
  showAddProxy.value = false
  await loadProxies()
}

// Обработчик после создания сессии
async function onSessionCreated() {
  showAddSession.value = false
  await loadSessions()
}

// Отправка команды на открытие сессии в ноде
async function openSessionInNode(session) {
  try {
    await fetch('http://localhost:3001/open-session', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(session),
    })
    alert('Команда на открытие сессии отправлена')
  } catch (e) {
    alert('Ошибка при отправке команды на ноду')
  }
}

// Автозагрузка при старте
onMounted(async () => {
  await loadProxies()
  await loadSessions()
})
</script>

<style>
.container {
  max-width: 800px;
  margin: 0 auto;
  font-family: Arial, sans-serif;
}

.buttons > button {
  margin-right: 10px;
}
</style>
