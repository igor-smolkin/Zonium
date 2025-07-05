<template>
  <div class="modal">
    <h2>Добавить прокси</h2>
    <form @submit.prevent="submit">
      <input v-model="form.name" placeholder="Name" required />
      <input v-model="form.host" placeholder="Host" required />
      <input v-model.number="form.port" placeholder="Port" type="number" required />
      <input v-model="form.username" placeholder="Username" />
      <input v-model="form.password" placeholder="Password" type="password" />
      <button type="submit">Сохранить</button>
      <button @click.prevent="$emit('cancel')">Отмена</button>
    </form>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
const emit = defineEmits(['created', 'cancel'])

const form = reactive({
  name: '', host: '', port: null, username: '', password: '',
})

async function submit() {
  await fetch('http://localhost:8080/api/proxy', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(form),
  })
  emit('created')
  Object.assign(form, { name: '', host: '', port: null, username: '', password: '' })
}
</script>
