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
import draggable from "vuedraggable";
import axios from "axios";

export default {
  name: "KanbanBoard",
  components: {
    draggable,
  },
  data() {
    return {
      columns: [],
      newTaskNameModal: "",
      isCreatePopupActive: false,
      selectedTask: null,
      selectedColumnIndex: null,
      isTaskPopupActive: false,
      searchQuery: "",
      filteredTasks: [],
    };
  },
  mounted() {
    this.fetchTasks();
  },
  watch: {
    searchQuery() {
      this.filterTasks();
    },
  },
  methods: {
    fetchTasks() {
      axios
        .get("http://localhost:7272/api/columns")
        .then((response) => {
          this.columns = response.data;
          this.filteredTasks = this.columns.map((column) => column.tasks);
        })
        .catch((error) => {
          console.error("Error fetching tasks:", error);
        });
    },
    handleTaskDrop(event) {
      const droppedTask = event.item;
      const fromColumnIndex = event.from.parentElement.dataset.columnIndex;
      const toColumnIndex = event.to.parentElement.dataset.columnIndex;
      console.log(`Task "${droppedTask.name}" dropped from column ${fromColumnIndex} to column ${toColumnIndex}`);
    },
    openCreatePopup(columnIndex) {
      this.isCreatePopupActive = true;
      this.newTaskNameModal = "";
      this.createPopupColumnIndex = columnIndex; 
    },
    createTask() {
  const columnIndex = this.createPopupColumnIndex;
  const column = this.columns[columnIndex];
  
  const newTask = {
    id: Date.now(),
    name: this.newTaskNameModal,
  };

  axios.post(`http://localhost:7272/api/columns/task/Backlog`, newTask)
    .then(() => {
      column.tasks.push(newTask);
      this.newTaskNameModal = "";
      this.filterTasks();
      this.closeCreatePopup();
    })
    .catch((error) => {
      console.error("Error creating task:", error);
    });
},
    openTaskPopup(task) {
      this.selectedTask = task;
      this.isTaskPopupActive = true;
      this.updatedTaskName = task.name;
      const columnIndex = this.columns.findIndex((column) => column.tasks.includes(task));
      this.selectedColumnIndex = columnIndex;
    },
    updateTaskInPopup() {
  const columnIndex = this.selectedColumnIndex;
  const column = this.columns[columnIndex];
  const taskIndex = column.tasks.findIndex((task) => task.id === this.selectedTask.id);
  
  const updatedTask = {
    id: this.selectedTask.id,
    name: this.updatedTaskName,
  };

  axios.put(`http://localhost:7272/api/columns/task/${updatedTask.id}`, updatedTask)
    .then(() => {
      column.tasks.splice(taskIndex, 1, updatedTask);
      this.filterTasks();
      this.closeTaskPopup();
    })
    .catch((error) => {
      console.error("Error updating task:", error);
    });
},
    deleteTask(columnIndex, taskId) {
  const column = this.columns[columnIndex];
  const taskIndex = column.tasks.findIndex((task) => task.id === taskId);
  
  // Make an API call to delete the task
  axios
    .delete(`http://localhost:7272/api/columns/task/${taskId}`)
    .then(() => {
      column.tasks.splice(taskIndex, 1);
      this.filterTasks();
      this.closeTaskPopup();
    })
    .catch((error) => {
      console.error("Error deleting task:", error);
    });
},

    closeCreatePopup() {
      this.isCreatePopupActive = false;
    },
    closeTaskPopup() {
      this.isTaskPopupActive = false;
      this.selectedTask = null;
      this.selectedColumnIndex = null;
      this.updatedTaskName = "";
    },
    filterTasks() {
      const searchQuery = this.searchQuery.toLowerCase();
      this.filteredTasks = this.columns.map((column) =>
        column.tasks.filter((task) => task.name.toLowerCase().includes(searchQuery))
      );
    },
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


