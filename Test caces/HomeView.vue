<template>
  <main>
    <div>
      <input type="text" v-model="searchQuery" placeholder="Search tasks" />
    </div>

    <div class="kanban-board">
      <div v-for="(column, columnIndex) in columns" :key="columnIndex" class="kanban-column">
        <h3>{{ column.title }}</h3>
        <draggable v-model="filteredTasks[columnIndex]" class="task-list" group="kanban" @end="handleTaskDrop">
          <div v-for="task in filteredTasks[columnIndex]" :key="task.id" class="task" @dblclick="openTaskPopup(task)">
            {{ task.name }}
          </div>
        </draggable>
        <button v-if="column.title === 'Backlog'" @click="openCreatePopup(columnIndex)">Add Task</button>
      </div>

      <div v-if="isCreatePopupActive" class="popup-overlay">
        <div class="popup-content">
          <input type="text" v-model="newTaskName" placeholder="Task name" />
          <button @click="createTask">Create</button>
          <button @click="closeCreatePopup">Cancel</button>
        </div>
      </div>

      <div v-if="isTaskPopupActive" class="popup-overlay">
        <div class="popup-content">
          <h3>{{ selectedTask.name }}</h3>
          <input type="text" v-model="updatedTask.name" />
          <button @click="updateTaskInPopup">Update</button>
          <button @click="closeTaskPopup">Close</button>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import { defineComponent, ref, watch } from 'vue';
import draggable from 'vuedraggable';
import { useKanbanStore } from '../stores/TaskStore';

export default defineComponent({
  name: 'KanbanBoard',
  components: {
    draggable,
  },
  setup() {
    const taskStore = useKanbanStore();

    const searchQuery = ref('');
    const columns = taskStore.columns;
    const filteredTasks = ref([]);

    const isCreatePopupActive = ref(false);
    const newTaskName = ref('');
    const selectedTask = ref(null);
    const selectedColumnIndex = ref(null);
    const isTaskPopupActive = ref(false);
    const updatedTask = ref({});

    watch(searchQuery, () => {
      filterTasks();
    });

    function handleTaskDrop(event) {
      // ...your implementation...
    }

    function openCreatePopup(columnIndex) {
      selectedColumnIndex.value = columnIndex;
      isCreatePopupActive.value = true;
    }

    function createTask() {
      if (newTaskName.value.trim() !== '') {
        taskStore.createTask(selectedColumnIndex.value, newTaskName.value);
        newTaskName.value = '';
        closeCreatePopup();
      }
    }

    function openTaskPopup(task) {
      selectedTask.value = task;
      updatedTask.value = { ...task };
      isTaskPopupActive.value = true;
    }

    function updateTaskInPopup() {
      if (selectedTask.value && updatedTask.value.name.trim() !== '') {
        taskStore.updateTask(selectedTask.value.id, updatedTask.value.name);
        closeTaskPopup();
      }
    }

    function deleteTask(columnIndex, taskId) {
      taskStore.deleteTask(columnIndex, taskId);
    }

    function closeCreatePopup() {
      isCreatePopupActive.value = false;
    }

    function closeTaskPopup() {
    isTaskPopupActive.value = false;
    selectedTask.value = null;
    updatedTask.value = {};
  }

  function filterTasks() {
    filteredTasks.value = taskStore.filterTasks(searchQuery.value);
  }

  return {
    searchQuery,
    columns,
    filteredTasks,
    isCreatePopupActive,
    newTaskName,
    selectedTask,
    selectedColumnIndex,
    isTaskPopupActive,
    updatedTask,
    handleTaskDrop,
    openCreatePopup,
    createTask,
    openTaskPopup,
    updateTaskInPopup,
    deleteTask,
    closeCreatePopup,
    closeTaskPopup,
    filterTasks,
  };
},
});
</script>

<style>
.kanban-board {
display: flex;
}

.kanban-column {
flex: 1;
margin: 1rem;
padding: 1rem;
border: 1px solid #ccc;
}

.task-list {
margin-top: 1rem;
}

.task {
background-color: #f0f0f0;
margin-bottom: 0.5rem;
padding: 0.5rem;
border: 1px solid #ccc;
border-radius: 4px;
cursor: move;
}

.popup-overlay {
position: fixed;
top: 0;
left: 0;
width: 100%;
height: 100%;
background-color: rgba(0, 0, 0, 0.5);
display: flex;
align-items: center;
justify-content: center;
}

.popup-content {
background-color: #fff;
padding: 1rem;
border-radius: 4px;
}
</style>

