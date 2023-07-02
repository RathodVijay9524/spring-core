<template>
    <div>
      <div class="controls">
        <input type="text" v-model="newTask" placeholder="Enter task name">
        <button @click="addTask">Add Task</button>
        <input type="text" v-model="searchText" placeholder="Search task">
      </div>
      <div class="kanban-board">
        <div v-for="(column, columnIndex) in columns" :key="columnIndex" class="kanban-column">
          <h3>{{ column.title }}</h3>
          <draggable v-model="column.tasks" class="task-list" group="kanban" @end="handleTaskDrop">
            <div v-for="(task, taskIndex) in filteredTasks(column.tasks)" :key="task.id" class="task">
              <span>{{ task.name }}</span>
              <button @click="editTask(columnIndex, taskIndex)">Edit</button>
              <button @click="deleteTask(columnIndex, taskIndex)">Delete</button>
            </div>
          </draggable>
        </div>
      </div>
      <div v-if="selectedTask !== null" class="edit-task">
        <input type="text" v-model="selectedTask.editedName" placeholder="Enter task name">
        <button @click="updateTask">Save</button>
      </div>
    </div>
  </template>
  
  <script>
  import draggable from "vuedraggable";
  
  export default {
    name: "KanbanBoard",
    components: {
      draggable,
    },
    data() {
      return {
        newTask: "",
        searchText: "",
        columns: [
          { title: "Backlog", tasks: [] },
          { title: "In Progress", tasks: [] },
          { title: "Testing", tasks: [] },
          { title: "Done", tasks: [] },
        ],
        selectedTask: null,
      };
    },
    methods: {
      addTask() {
        if (this.newTask) {
          this.columns[0].tasks.push({ id: Date.now(), name: this.newTask });
          this.newTask = "";
        }
      },
      editTask(columnIndex, taskIndex) {
        const task = this.columns[columnIndex].tasks[taskIndex];
        this.selectedTask = {
          columnIndex,
          taskIndex,
          originalName: task.name,
          editedName: task.name,
        };
      },
      updateTask() {
        if (this.selectedTask !== null) {
          const { columnIndex, taskIndex, editedName } = this.selectedTask;
          this.columns[columnIndex].tasks[taskIndex].name = editedName;
          this.selectedTask = null;
        }
      },
      deleteTask(columnIndex, taskIndex) {
        this.columns[columnIndex].tasks.splice(taskIndex, 1);
      },
      filteredTasks(tasks) {
        if (!this.searchText) {
          return tasks;
        }
        const searchTerm = this.searchText.toLowerCase();
        return tasks.filter((task) => task.name.toLowerCase().includes(searchTerm));
      },
      handleTaskDrop() {
        console.log("Task dropped");
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
    margin: 0 10px;
  }
  
  .controls {
    margin-bottom: 10px;
  }
  
  .task {
    display: flex;
    align-items: center;
    padding: 5px;
    margin-bottom: 5px;
    background-color: #f1f1f1;
  }
  
  .task-list {
    min-height: 100px;
    padding: 10px;
    background-color: #f9f9f9;
  }
  </style>