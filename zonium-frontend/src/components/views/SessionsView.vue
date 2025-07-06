<template>
  <div class="container">
    <h1>Управление сессиями</h1>

    <button @click="showAddSession = true">Создать сессию</button>

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
import AddSessionModal from '../sessions/AddSessionModal.vue'
import SessionList from '../sessions/SessionList.vue'

const showAddSession = ref(false)
const proxies = ref([])
const sessions = ref([])

async function loadProxies() {
  const res = await fetch('http://localhost:8080/api/proxy', {
    credentials: 'include'
  })
  if (!res.ok) {
    throw new Error(`Ошибка при загрузке прокси: ${res.status}`)
  }
  proxies.value = await res.json()
}

async function loadSessions() {
  const res = await fetch('http://localhost:8080/api/session', {
    credentials: 'include'
  })
  if (!res.ok) {
    throw new Error(`Ошибка при загрузке сессий: ${res.status}`)
  }
  sessions.value = await res.json()
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
    alert('Ошибка при отправке команды на ноду')
  }
}

onMounted(async () => {
  await loadProxies()
  await loadSessions()
})
</script>
