<template>
  <div class="container">
    <div class="header">
      <h1>Управление прокси</h1>
      <button class="circle-btn" @click="showAddProxy = true">+</button>
    </div>
    <AddProxyModal
        v-if="showAddProxy"
        @created="onProxyCreated"
        @cancel="showAddProxy = false"
    />

    <ProxyList :proxies="proxies"/>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import AddProxyModal from '../proxies/AddProxyModal.vue'
import ProxyList from '../proxies/ProxyList.vue'

const showAddProxy = ref(false)
const proxies = ref([])

async function loadProxies() {
  const res = await fetch('http://localhost:8080/api/proxy', { credentials: 'include' })
  if (!res.ok) throw new Error(`Ошибка при загрузке прокси: ${res.status}`)
  proxies.value = await res.json()
}

onMounted(loadProxies)

async function onProxyCreated() {
  showAddProxy.value = false
  await loadProxies()
}

onMounted(() => loadProxies())
</script>

<style scoped>
.container {
  padding: 1rem;
}

.header {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1rem;
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

