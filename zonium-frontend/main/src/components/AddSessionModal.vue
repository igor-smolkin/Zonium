<template>
  <div class="modal">
    <h2>Создать сессию</h2>
    <form @submit.prevent="submit">
      <select v-model="form.proxyId" required>
        <option disabled value="">Выберите прокси</option>
        <option v-for="proxy in proxies" :key="proxy.id" :value="proxy.id">
          {{ proxy.name }} ({{ proxy.host }}:{{ proxy.port }})
        </option>
      </select>
      <button type="submit">Создать</button>
      <button @click.prevent="$emit('cancel')">Отмена</button>
    </form>
  </div>
</template>

<script setup>
import { reactive } from 'vue'

const props = defineProps({
  proxies: Array,
})
const emit = defineEmits(['created', 'cancel'])

const form = reactive({
  proxyId: '',
  fingerprint: '',
  userAgent: '',
})

async function submit() {
  await fetch('http://localhost:8080/api/session', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(form),
  })
  emit('created')
  Object.assign(form, { proxyId: '', fingerprint: '', userAgent: '' })
}
</script>
