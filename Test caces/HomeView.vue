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
    <form @submit.prevent="createTask">
      <div class="form-group">
        <label for="issueType">Issue Type:</label>
    <select id="issueType" v-model="newTaskIssueType">
      <option value="bug">Bug</option>
      <option value="task">Task</option>
      <option value="story">Story</option>
    </select>
      </div>
      <div class="form-group">
        <label for="taskName">Task Name:</label>
        <input type="text" id="taskName" v-model="newTaskNameModal" placeholder="Task Name" />
      </div>
      <div class="form-group">
        <label for="sortSummary">Sort Summary:</label>
        <input type="text" id="sortSummary" v-model="newTaskSortSummary" placeholder="Sort Summary" />
      </div>
      <div class="form-group">
        <label for="description">Description:</label>
        <textarea id="description" v-model="newTaskDescription" placeholder="Description"></textarea>
      </div>
      <div class="form-group">
        <label for="priority">Priority:</label>
      <select id="priority" v-model="newTaskPriority" :class="{ 'selected': newTaskPriority !== '' }">
      <option value="" disabled>Select Priority</option>
      <option value="highest">Highest</option>
      <option value="high">High</option>
      <option value="medium">Medium</option>
      <option value="low">Low</option>
    </select>
      </div>
      <div class="form-group">
        <label for="assignees">Assignees:</label>
        <input type="text" id="assignees" v-model="newTaskAssignees" placeholder="Assignees" />
      </div>
      <div class="form-group">
        <button type="submit">Add</button>
        <button type="button" @click="closeCreatePopup">Cancel</button>
      </div>
    </form>
  </div>
</div>



<div v-if="isTaskPopupActive" class="popup-overlay">
  <div class="popup-content">
    <button @click="deleteTask(selectedColumnIndex, selectedTask.id)"><i class="fas fa-trash"></i></button><br/><br/>
    <h3>{{ selectedTask.name }}</h3>

    <form>
        <div class="form-group">
          <label for="updateTaskName">New Task Name:</label>
          <input type="text" id="updateTaskName" v-model="updatedTask.name" placeholder="New Task Name" />
        </div>
        <div class="form-group">
          <label for="updateTaskIssueType">Issue Type:</label>
          <select id="updateTaskIssueType" v-model="updatedTask.issueType">
            <option value="bug">Bug</option>
            <option value="task">Task</option>
            <option value="story">Story</option>
          </select>
        </div>
        <div class="form-group">
          <label for="updateTaskPriority">Priority:</label>
          <select id="updateTaskPriority" v-model="updatedTask.priority">
            <option value="highest">Highest</option>
            <option value="high">High</option>
            <option value="low">Low</option>
          </select>
        </div>
        <div class="form-group">
          <label for="updateTaskAssignees">Assignees:</label>
          <input type="text" id="updateTaskAssignees" v-model="updatedTask.assignees" placeholder="Assignees" />
        </div>
      </form>


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
      newTaskIssueType: "bug", // Default issue type
      newTaskPriority: "highest", // Default priority
      newTaskAssignees: "", // Default assignees

      isCreatePopupActive: false,
      selectedTask: null,
      selectedColumnIndex: null,
      isTaskPopupActive: false,
      searchQuery: "",
      filteredTasks: [],
      updatedTaskName: "",
      updatedTaskIssueType: "",
      updatedTaskPriority: "",
      updatedTaskAssignees: "",
      updatedTask: {},
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
      this.newTaskSortSummary = "";
      this.newTaskDescription = "";
      this.newTaskNameModal = "";
      this.newTaskIssueType = "bug";
      this.newTaskPriority = "highest";
      this.newTaskAssignees = "";
      this.createPopupColumnIndex = columnIndex;
    },
    createTask() {
      const columnIndex = this.createPopupColumnIndex;
      const column = this.columns[columnIndex];

      const newTask = {
        id: Date.now(),
        sortSummary: this.newTaskSortSummary,
        description: this.newTaskDescription,
        name: this.newTaskNameModal,
        issueType: this.newTaskIssueType,
        priority: this.newTaskPriority,
        assignees: this.newTaskAssignees,
      };

      axios
        .post(`http://localhost:7272/api/columns/task/Backlog`, newTask)
        .then(() => {
          column.tasks.push(newTask);
          this.newTaskSortSummary = "";
          this.newTaskDescription = "";
          this.newTaskNameModal = "";
          this.newTaskIssueType = "bug";
          this.newTaskPriority = "highest";
          this.newTaskAssignees = "";
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
      this.updatedTask.id = task.id;
      this.updatedTask.name = task.name;
      this.updatedTask.issueType = task.issueType;
      this.updatedTask.priority = task.priority;
      this.updatedTask.assignees = task.assignees;
      const columnIndex = this.columns.findIndex((column) => column.tasks.includes(task));
      this.selectedColumnIndex = columnIndex;
    },
    updateTaskInPopup() {
      const columnIndex = this.selectedColumnIndex;
      const column = this.columns[columnIndex];
      const taskIndex = column.tasks.findIndex((task) => task.id === this.selectedTask.id);

      axios
        .put(`http://localhost:7272/api/columns/task/${this.updatedTask.id}`, this.updatedTask)
        .then(() => {
          column.tasks.splice(taskIndex, 1, this.updatedTask);
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
  background-color: #fff;
  padding: 20px;
  border-radius: 5px;
  max-width: 500px; /* Adjust the max-width as needed */
  width: 80%; /* Adjust the width as needed */
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

/* Additional styles for the form fields inside the popup */
.popup-content form {
  display: flex;
  flex-direction: column;
}

.popup-content .form-group {
  margin-bottom: 15px;
}

.popup-content label {
  font-weight: bold;
}


.popup-content input[type="text"],
.popup-content textarea {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 3px;
  box-sizing: border-box;
}

.popup-content select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 3px;
  box-sizing: border-box;
}

.popup-content button {
  margin-top: 10px;
  padding: 8px 15px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 3px;
  cursor: pointer;
}

.popup-content button[type="submit"] {
  background-color: #28a745;
}
.popup-content select.selected {
  border-color: red;
}
</style>


