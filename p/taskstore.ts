// tasks store
import { defineStore } from 'pinia';
import axios from 'axios';

export const useTasksStore = defineStore('tasks', {
  state: () => ({
    columns: [],
    newTaskNameModal: '',
    isCreatePopupActive: false,
    selectedTask: null,
    selectedColumnIndex: null,
    isTaskPopupActive: false,
  }),

  actions: {
    fetchTasks() {
      return axios.get('http://localhost:7272/api/columns')
        .then((response) => {
          this.columns = response.data;
        });
    },

    openCreatePopup(columnIndex) {
      this.isCreatePopupActive = true;
      this.newTaskNameModal = '';
      this.selectedColumnIndex = columnIndex;
    },

    createTask() {
      const columnIndex = this.selectedColumnIndex;
      const column = this.columns[columnIndex];

      const newTask = {
        id: Date.now(),
        name: this.newTaskNameModal,
      };

      return axios.post(`http://localhost:7272/api/columns/task/Backlog`, newTask)
        .then(() => {
          column.tasks.push(newTask);
          this.newTaskNameModal = '';
          this.filterTasks();
          this.isCreatePopupActive = false;
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

      return axios.put(`http://localhost:7272/api/columns/task/${updatedTask.id}`, updatedTask)
        .then(() => {
          column.tasks.splice(taskIndex, 1, updatedTask);
          this.filterTasks();
          this.isTaskPopupActive = false;
        });
    },

    deleteTask(columnIndex, taskId) {
        const column = this.columns[columnIndex];
        const taskIndex = column.tasks.findIndex((task) => task.id === taskId);
      
        return axios.delete(`http://localhost:7272/api/columns/task/${taskId}`)
          .then(() => {
            column.tasks.splice(taskIndex, 1);
            this.filterTasks();
            this.isTaskPopupActive = false;
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
      this.updatedTaskName = '';
    },

    filterTasks() {
      const searchQuery = this.searchQuery.toLowerCase();
      this.filteredTasks = this.columns.map((column) =>
        column.tasks.filter((task) => task.name.toLowerCase().includes(searchQuery))
      );
    },
  },
});
