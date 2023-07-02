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
  
        
      </div>
    </main>
  </template>
  
  <script lang="ts">
  import { defineComponent, ref, watch } from "vue";
  import draggable from "vuedraggable";
  import { useTasksStore } from "@/stores/taskstore";
  
  export default defineComponent({
    name: "KanbanBoard",
    components: {
      draggable,
    
    },
    setup() {
      const tasksStore = useTasksStore();
      const searchQuery = ref("");
      const filteredTasks = ref([]);
      const updatedTaskName = ref('');
  
      const fetchTasks = () => {
        tasksStore.fetchTasks()
          .then(() => {
            filterTasks();
          })
          .catch((error) => {
            console.error("Error fetching tasks:", error);
          });
      };
  
      const handleTaskDrop = (event) => {
        const droppedTask = event.item;
        const fromColumnIndex = event.from.parentElement.dataset.columnIndex;
        const toColumnIndex = event.to.parentElement.dataset.columnIndex;
        console.log(`Task "${droppedTask.name}" dropped from column ${fromColumnIndex} to column ${toColumnIndex}`);
      };
  
      const openCreatePopup = (columnIndex) => {
        tasksStore.openCreatePopup(columnIndex);
      };
  
      const createTask = () => {
        tasksStore.createTask()
          .then(() => {
            searchQuery.value = "";
          })
          .catch((error) => {
            console.error("Error creating task:", error);
          });
      };
  
      const openTaskPopup = (task) => {
        tasksStore.openTaskPopup(task);
      };
  
      const updateTaskInPopup = () => {
        tasksStore.updateTaskInPopup()
          .then(() => {
            searchQuery.value = "";
          })
          .catch ((error) => {
          console.error("Error updating task:", error);
        });
    };

    

    const closeCreatePopup = () => {
      tasksStore.closeCreatePopup();
    };

    const closeTaskPopup = () => {
      tasksStore.closeTaskPopup();
    };

    const filterTasks = () => {
      const query = searchQuery.value.toLowerCase();
      filteredTasks.value = tasksStore.columns.map((column) =>
        column.tasks.filter((task) => task.name.toLowerCase().includes(query))
      );
    };

    watch(searchQuery, filterTasks);

    // Fetch tasks on component mount
    fetchTasks();

    return {
      searchQuery,
      filteredTasks,
      columns: tasksStore.columns,
      isCreatePopupActive: tasksStore.isCreatePopupActive,
      newTaskNameModal: tasksStore.newTaskNameModal,
      isTaskPopupActive: tasksStore.isTaskPopupActive,
      selectedTask: tasksStore.selectedTask,
      selectedColumnIndex: tasksStore.selectedColumnIndex,
      updatedTaskName,
      fetchTasks,
      handleTaskDrop,
      openCreatePopup,
      createTask,
      openTaskPopup,
      updateTaskInPopup,
      closeCreatePopup,
      closeTaskPopup,
      
    };
  },
});
</script>
  