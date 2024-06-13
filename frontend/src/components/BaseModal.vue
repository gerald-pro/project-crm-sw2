<script setup lang="ts">

const props = defineProps({
    open: {
        type: Boolean,
        required: true
    }
})

const emit = defineEmits(['close'])

</script>

<template>
    <div>
        <div
            :class="`modal ${!props.open && 'opacity-0 pointer-events-none'} z-50 fixed w-full h-full top-0 left-0 flex items-center justify-center`">
            <div class="absolute w-full h-full bg-gray-900 opacity-50 modal-overlay" @click="$emit('close')" />

            <div class="z-50 w-11/12 mx-auto overflow-y-auto bg-white rounded shadow-lg modal-container md:max-w-md">
                <div class="px-6 py-4 text-left modal-content">
                    <!-- Title -->
                    <slot name="title">
                        <div class="flex items-center justify-between pb-3">
                            <p class="text-2xl font-bold">
                                Modal Title
                            </p>
                            <div class="z-50 cursor-pointer modal-close" @click="$emit('close')">
                                <font-awesome-icon icon="x" />
                            </div>
                        </div>
                    </slot>

                    <!-- Body -->
                    <slot name="body">
                        <p>Modal content.</p>
                    </slot>

                    <!-- Footer -->
                    <slot name="footer">
                        <div class="flex justify-end pt-2">
                            <!-- <button
                                class="p-3 px-6 py-3 mr-2 text-indigo-500 bg-transparent rounded-lg hover:bg-gray-100 hover:text-indigo-400 focus:outline-none"
                                @click="$emit('close')">
                                Close
                            </button>
                            <button
                                class="px-6 py-3 font-medium tracking-wide text-white bg-indigo-600 rounded-md hover:bg-indigo-500 focus:outline-none"
                                @click="$emit('close')">
                                Action
                            </button> -->
                        </div>
                    </slot>
                </div>
            </div>
        </div>
    </div>
</template>

<style>
.modal {
    transition: opacity 0.25s ease;
}
</style>