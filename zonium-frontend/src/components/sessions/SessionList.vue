<template>
  <div class="session-list" v-if="sessions.length > 0">
    <ul>
      <div>
        <table class="session-table">
          <thead>
          <tr>
            <th>Название сессии</th>
            <th>IP-адрес прокси</th>
            <th>Статус сессии</th>
            <th>Дата создания</th>
            <th>Fingerprint</th>
            <th>User-Agent</th>
            <th>Действия</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="session in sessions" :key="session.id">
            <td>{{ session.name }}</td>
            <td>{{ getProxyIp(session.proxyId) }}</td>
            <td>{{ session.status }}</td>
            <td>{{ formatDate(session.createdAt) }}</td>
            <td>{{ session.fingerprint }}</td>
            <td>{{ session.userAgent }}</td>
            <td>
              <button @click="$emit('open-session', session)">Открыть браузер</button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </ul>
  </div>
</template>

<script setup>
const props = defineProps({
  sessions: Array,
  proxies: Array,
})

function getProxyIp(id) {
  const proxy = props.proxies.find(p => p.id === id)
  return proxy ? `${proxy.host}:${proxy.port}` : '—'
}

function formatDate(dateString) {
  const date = new Date(dateString)
  return date.toLocaleString()
}
</script>


<style scoped>
.session-table {
  width: 100%;
  border-collapse: collapse;
}

.session-table th,
.session-table td {
  padding: 8px 12px;
  border: 1px solid #ccc;
  text-align: center;
}

.session-table th {
  background-color: #f5f5f5;
}

.session-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
}
</style>
