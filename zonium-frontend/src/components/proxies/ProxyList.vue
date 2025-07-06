<template>
  <div class="proxy-list" v-if="proxies.length > 0">
    <h3>Прокси:</h3>
    <ul>
      <li v-for="proxy in proxies" :key="proxy.id">
        {{ proxy.name }} — {{ proxy.host }}:{{ proxy.port }}
      </li>
    </ul>
  </div>
</template>

<script setup>
defineProps({
  proxies: Array,
})
</script>
