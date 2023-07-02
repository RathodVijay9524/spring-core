<template>
    <div class="container mt-5">
      <div class="row">
        <div class="col form-inline">
          <b-form-input
            id="input-2"
            v-model="newTask"
            required
            placeholder="Enter Task"
            @keyup.enter="add"
          ></b-form-input>
          <b-button @click="add" variant="primary" class="ml-3">Add</b-button>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <b-form-input
            id="input-3"
            v-model="searchText"
            placeholder="Search Task"
          ></b-form-input>
        </div>
      </div>
      <div class="row mt-5">
        <div class="col-3">
          <div class="p-2 alert alert-secondary">
            <h3>Back Log</h3>
            <draggable
              class="list-group kanban-column"
              :list="filteredBacklog"
              group="tasks"
            >
              <div
                class="list-group-item"
                v-for="element in filteredBacklog"
                :key="element.id"
              >
                <div v-if="selectedTask !== element">
                  {{ element.name }}
                  <div class="float-right">
                    <b-button size="sm" @click="editTask(element)">+</b-button>&nbsp;&nbsp;&nbsp;
                    <b-button size="sm" @click="deleteTask(arrBackLog, element)">X</b-button>
                  </div>
                </div>
                <div v-else>
                  <b-form-input v-model="element.name" @keyup.enter="updateTask" />
                  <b-button size="sm" @click="updateTask">Update</b-button>
                </div>
              </div>
            </draggable>
          </div>
        </div>
  
        <div class="col-3">
          <div class="p-2 alert alert-primary">
            <h3>In Progress</h3>
            <draggable
              class="list-group kanban-column"
              :list="arrInProgress"
              group="tasks"
            >
              <div
                class="list-group-item"
                v-for="element in arrInProgress"
                :key="element.id"
              >
                {{ element.name }}
                <div class="float-right">
                  <b-button size="sm" @click="deleteTask(arrInProgress, element)">Delete</b-button>
                </div>
              </div>
            </draggable>
          </div>
        </div>
  
        <div class="col-3">
          <div class="p-2 alert alert-warning">
            <h3>Testing</h3>
            <draggable
              class="list-group kanban-column"
              :list="arrTested"
              group="tasks"
            >
              <div
                class="list-group-item"
                v-for="element in arrTested"
                :key="element.id"
              >
                {{ element.name }}
                <div class="float-right">
                  <b-button size="sm" @click="deleteTask(arrTested, element)">Delete</b-button>
                </div>
              </div>
            </draggable>
          </div>
        </div>
  
        <div class="col-3">
          <div class="p-2 alert alert-success">
            <h3>Done</h3>
            <draggable
              class="list-group kanban-column"
              :list="arrDone"
              group="tasks"
            >
            <div
                class="list-group-item"
                v-for="element in arrDone"
                :key="element.id"
              >
              {{ element.name }}
              <div class="float-right">
                <b-button size="sm" @click="deleteTask(arrDone, element)">Delete</b-button>
              </div>
            </div>
          </draggable>
        </div>
      </div>
    </div>
  </div>
  </template>
  
  <script>
  import draggable from "vuedraggable";
  
  export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Board",
  components: {
    draggable
  },
  data() {
    return {
      newTask: "",
      arrBackLog: [
        { id: 1, name: "Code Sign Up Page" },
        { id: 2, name: "Test Dashboard" },
        { id: 3, name: "Style Registration" },
        { id: 4, name: "Help with Designs" }
      ],
      arrInProgress: [],
      arrTested: [],
      arrDone: [],
      selectedTask: null,
      searchText: ""
    };
  },
  methods: {
    add() {
      if (this.newTask) {
        this.arrBackLog.push({ id: Date.now(), name: this.newTask });
        this.newTask = "";
      }
    },
    editTask(task) {
      this.selectedTask = task;
    },
    updateTask() {
      this.selectedTask = null;
    },
    deleteTask(taskArray, task) {
      const index = taskArray.findIndex(item => item.id === task.id);
      if (index !== -1) {
        taskArray.splice(index, 1);
      }
    }
  },
  computed: {
    filteredBacklog() {
      if (!this.searchText) {
        return this.arrBackLog;
      }
      const searchTerm = this.searchText.toLowerCase();
      return this.arrBackLog.filter(task =>
        task.name.toLowerCase().includes(searchTerm)
      );
    }
  }
  };
  </script>
  
  <style>
  /* light stylings for the kanban columns */
  .kanban-column {
  min-height: 300px;
  }
  </style>
  