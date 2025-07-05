<template>
  <div class="session-list" v-if="sessions.length > 0">
    <h3>Сессии:</h3>
    <ul>
      <li v-for="session in sessions" :key="session.id" class="session-item">
        <div>
          ID: {{ session.id }}, Proxy: {{ getProxyName(session.proxyId) }}, UA: {{ session.userAgent }}
        </div>
        <button @click="$emit('open-session', session)">Открыть в ноде</button>
      </li>
    </ul>
  </div>
</template>

<script setup>
const props = defineProps({
  sessions: Array,
  proxies: Array,
})

function getProxyName(id) {
  const proxy = props.proxies.find(p => p.id === id)
  return proxy ? proxy.name : 'Unknown'
}
</script>

<style scoped>
.session-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
}
</style>
