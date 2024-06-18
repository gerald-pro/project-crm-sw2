<!-- LeadCard.vue -->
<template>
  <div class="bg-white p-4 rounded shadow-md mb-3 border border-gray-300">
    <div class="flex items-center justify-between">

      <h3 class="text-md font-semibold">
        <router-link :to="{ name: 'ShowLead', params: { id: lead.id } }" class="text-teal-600 hover:underline">
          {{ lead.title }}
        </router-link>
      </h3>
      <span class="text-md font-bold text-gray-900">BOB {{ lead.value }}</span>
    </div>
    <p class="text-gray-600">{{ lead.description }}</p>
    <div class="mt-4 flex justify-end space-x-2">
      <button
        class="px-4 py-1 text-indigo-500 bg-transparent rounded-lg hover:bg-gray-100 hover:text-indigo-400 focus:outline-none"
        @click="openEditModal(lead)">
        <font-awesome-icon icon="pen" />
      </button>
      <button
        class="px-4 py-1 text-indigo-500 bg-transparent rounded-lg hover:bg-gray-100 hover:text-indigo-400 focus:outline-none"
        @click="handleDeleteLead(lead.id)">
        <font-awesome-icon icon="trash" />
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineEmits } from 'vue';

const props = defineProps({
  lead: {
    type: Object,
    required: true,
  },
});

const emit = defineEmits(['editLead', 'deleteLead']);

const openEditModal = (lead: any) => {
  emit('editLead', lead);
};

const handleDeleteLead = (id: any) => {
  emit('deleteLead', id);
};
</script>