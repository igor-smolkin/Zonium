<template>
  <div class="container">
    <div class="header">
      <h1>Управление сессиями</h1>
      <button class="circle-btn" @click="showAddSession = true">+</button>
    </div>
    <AddSessionModal
        v-if="showAddSession"
        :proxies="proxies"
        @created="onSessionCreated"
        @cancel="showAddSession = false"
    />
    <SessionList
        :sessions="sessions"
        :proxies="proxies"
        @open-session="openSessionInNode"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import AddSessionModal from "../sessions/AddSessionModal.vue"
import SessionList from "../sessions/SessionList.vue"

const showAddSession = ref(false)
const sessions = ref([])
const proxies = ref([])

async function loadSessions() {
  const res = await fetch('http://localhost:8080/api/session', {
    credentials: 'include',
  })
  if (!res.ok) throw new Error(`Ошибка при загрузке сессий: ${res.status}`)
  sessions.value = await res.json()
}

async function loadProxies() {
  const res = await fetch('http://localhost:8080/api/proxy', {
    credentials: 'include',
  })
  if (!res.ok) throw new Error(`Ошибка при загрузке прокси: ${res.status}`)
  proxies.value = await res.json()
}

async function onSessionCreated() {
  showAddSession.value = false
  await loadSessions()
}
async function openSessionInNode(session) {
  try {
    await fetch('http://localhost:3001/open-session', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify(session),
    })
    alert('Команда на открытие сессии отправлена')
  } catch {
    alert('Ошибка при отправке команды')
  }
}

onMounted(() => {
  loadSessions()
  loadProxies()
})
</script>

<style scoped>

.header {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1rem;
}

.container {
  padding: 1rem;
}

.circle-btn {
  width: 35px;
  height: 35px;
  border-radius: 50%;
  background-color: #42b983;
  color: white;
  border: none;
  font-size: 24px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.circle-btn:hover {
  background-color: #369c6f;
}
</style>
