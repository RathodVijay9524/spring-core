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
            <h3>Add New Task</h3>
            <input type="text" v-model="newTaskNameModal" placeholder="Task Name" />
            <button @click="createTask">Add</button>
            <button @click="closeCreatePopup">Cancel</button>
          </div>
        </div>
  
        <div v-if="isTaskPopupActive" class="popup-overlay">
          <div class="popup-content">
            <button @click="deleteTask(selectedColumnIndex, selectedTask.id)"><i class="fas fa-trash"></i></button><br/><br/>
            <h3>{{ selectedTask.name }}</h3>
            <input type="text" v-model="updatedTaskName" placeholder="New Task Name" />
            <button @click="updateTaskInPopup">Update</button>
            <button @click="closeTaskPopup">Close</button>
          </div>
        </div>
      </div>
    </main>
  </template>
  
  <script>
  import { ref } from 'vue';
  import { useStore } from 'pinia';
  import draggable from 'vuedraggable';
  
  export default {
    name: 'KanbanBoard',
    components: {
      draggable,
    },
    setup() {
      const searchQuery = ref('');
      const isCreatePopupActive = ref(false);
      const newTaskNameModal = ref('');
      const selectedTask = ref(null);
      const selectedColumnIndex = ref(null);
      const isTaskPopupActive = ref(false);
      const columns = useStore().columns;
      const filteredTasks = ref([]);
      const updatedTaskName = ref('');
  
      const handleTaskDrop = (event) => {
        const droppedTask = event.item;
        const fromColumnIndex = event.from.parentElement.dataset.columnIndex;
        const toColumnIndex = event.to.parentElement.dataset.columnIndex;
        console.log(`Task "${droppedTask.name}" dropped from column ${fromColumnIndex} to column ${toColumnIndex}`);
      };
  
      const openCreatePopup = (columnIndex) => {
        isCreatePopupActive.value = true;
        newTaskNameModal.value = '';
        selectedColumnIndex.value = columnIndex;
      };
  
      const createTask = () => {
        const columnIndex = selectedColumnIndex.value;
        const column = columns.value[columnIndex];
        const newTask = {
          id: Date.now(),
          name: newTaskNameModal.value,
        };
        column.tasks.push(newTask);
        newTaskNameModal.value = '';
        filterTasks();
        closeCreatePopup();
      };
  
      const openTaskPopup = (task) => {
        selectedTask.value = task;
        isTaskPopupActive.value = true;
        updatedTaskName.value = task.name;
        const columnIndex = columns.value.findIndex((column) => column.tasks.includes(task));
        selectedColumnIndex.value = columnIndex;
      };
  
      const updateTaskInPopup = () => {
        const columnIndex = selectedColumnIndex.value;
        const column = columns.value[columnIndex];
        const taskIndex = column.tasks.findIndex((task) => task.id === selectedTask.value.id);
        column.tasks[taskIndex].name = updatedTaskName.value;
        closeTaskPopup();
      };
  
      const deleteTask = (columnIndex, taskId) => {
        const column = columns.value[columnIndex];
        const taskIndex = column.tasks.findIndex((task) => task.id === taskId);
        column.tasks.splice(taskIndex, 1);
        closeTaskPopup();
      };
  
      const closeCreatePopup = () => {
        isCreatePopupActive.value = false;
      };
  
      const closeTaskPopup = () => {
        isTaskPopupActive.value = false;
        selectedTask.value = null;
        selectedColumnIndex.value = null;
        updatedTaskName.value = '';
      };
  
      const filterTasks = () => {
        const query = searchQuery.value.toLowerCase();
        filteredTasks.value = columns.value.map((column) =>
          column.tasks.filter((task) => task.name.toLowerCase().includes(query))
        );
      };
  
      return {
        searchQuery,
        isCreatePopupActive,
        newTaskNameModal,
        selectedTask,
        selectedColumnIndex,
        isTaskPopupActive,
        columns,
        filteredTasks,
        handleTaskDrop,
        openCreatePopup,
        createTask,
        openTaskPopup,
        updateTaskInPopup,
        deleteTask,
        closeCreatePopup,
        closeTaskPopup,
        filterTasks,
        updatedTaskName,
        
      };
    },
  };
  </script>
  
  
  
  
  
  <style>
  .kanban-board {
   display: flex;
  }
  
  .kanban-column {
   flex: 1;
   margin: 10px;
   padding: 10px;
   background-color: #f2f2f2;
  }
  
  .task-list {
   min-height: 200px;
   background-color: white;
   padding: 10px;
   border: 1px solid #ccc;
   border-radius: 4px;
  }
  
  .task {
   margin: 5px 0;
   padding: 5px;
   background-color: #f9f9f9;
   border: 1px solid #ccc;
   border-radius: 4px;
  }
    .popup-overlay {
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background-color: rgba(0, 0, 0, 0.5);
      display: flex;
      justify-content: center;
      align-items: center;
    }
    .popup-content {
      background-color: #ffffff;
      padding: 20px;
      border-radius: 5px;
    }
  </style>
  
  
  