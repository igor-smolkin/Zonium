<template>
  <div class="container">
    <h1>Управление прокси</h1>

    <button @click="showAddProxy = true">Добавить прокси</button>

    <AddProxyModal
        v-if="showAddProxy"
        @created="onProxyCreated"
        @cancel="showAddProxy = false"
    />

    <ProxyList :proxies="proxies" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import AddProxyModal from '../proxies/AddProxyModal.vue'
import ProxyList from '../proxies/ProxyList.vue'

const showAddProxy = ref(false)
const proxies = ref([])

async function loadProxies() {
  const res = await fetch('http://localhost:8080/api/proxy', {
    credentials: 'include'
  })
  if (!res.ok) {
    throw new Error(`Ошибка при загрузке прокси: ${res.status}`)
  }
  proxies.value = await res.json()
}

async function onProxyCreated() {
  showAddProxy.value = false
  await loadProxies()
}

onMounted(() => loadProxies())
</script>
