import { defineStore } from 'pinia';
import axios from 'axios';

export interface Task {
  id: number;
  sortSummary: string;
  description: string;
  name: string;
  issueType: string;
  priority: string;
  assignees: string;
}

export interface Column {
  tasks: Task[];
}

export interface KanbanState {
  columns: Column[];
  newTaskNameModal: string;
  newTaskIssueType: string;
  newTaskPriority: string;
  newTaskAssignees: string;
  isCreatePopupActive: boolean;
  selectedTask: Task | null;
  selectedColumnIndex: number | null;
  isTaskPopupActive: boolean;
  searchQuery: string;
  filteredTasks: Task[];
  updatedTask: Partial<Task>;
}

export const useKanbanStore = defineStore('kanban', {
  state: (): KanbanState => ({
    columns: [],
    newTaskNameModal: '',
    newTaskIssueType: 'bug',
    newTaskPriority: 'highest',
    newTaskAssignees: '',
    isCreatePopupActive: false,
    selectedTask: null,
    selectedColumnIndex: null,
    isTaskPopupActive: false,
    searchQuery: '',
    filteredTasks: [],
    updatedTask: {},
  }),

  actions: {
    async fetchTasks() {
      try {
        const response = await axios.get('http://localhost:7272/api/columns');
        this.columns = response.data;
        console.log(this.columns);
      } catch (error) {
        console.error('Error fetching tasks:', error);
      }
    },

    handleTaskDrop(event: any) {
      const droppedTask = event.item;
      const fromColumnIndex = event.from.parentElement.dataset.columnIndex;
      const toColumnIndex = event.to.parentElement.dataset.columnIndex;
      console.log(
        `Task "${droppedTask.name}" dropped from column ${fromColumnIndex} to column ${toColumnIndex}`
      );
    },

    openCreatePopup(columnIndex: number) {
      this.isCreatePopupActive = true;
      this.newTaskNameModal = '';
      this.newTaskIssueType = 'bug';
      this.newTaskPriority = 'highest';
      this.newTaskAssignees = '';
      this.selectedColumnIndex = columnIndex;
    },

    async createTask() {
      const columnIndex = this.selectedColumnIndex!;
      const column = this.columns[columnIndex];

      const newTask: Task = {
        id: Date.now(),
        sortSummary: '',
        description: '',
        name: this.newTaskNameModal,
        issueType: this.newTaskIssueType,
        priority: this.newTaskPriority,
        assignees: this.newTaskAssignees,
      };

      try {
        await axios.post(`http://localhost:7272/api/columns/task/Backlog`, newTask);
        column.tasks.push(newTask);
        this.closeCreatePopup();
      } catch (error) {
        console.error('Error creating task:', error);
      }
    },

    openTaskPopup(task: Task) {
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

    async updateTaskInPopup() {
        const columnIndex = this.selectedColumnIndex!;
        const column = this.columns[columnIndex];
        const taskIndex = column.tasks.findIndex((task) => task.id === this.selectedTask!.id);
      
        try {
          await axios.put(`http://localhost:7272/api/columns/task/${this.updatedTask.id}`, this.updatedTask);
          column.tasks.splice(taskIndex, 1, this.updatedTask as Task);
          this.closeTaskPopup();
        } catch (error) {
          console.error('Error updating task:', error);
        }
      },
      
    
    async deleteTask(columnIndex: number, taskId: number) {
      const column = this.columns[columnIndex];
      const taskIndex = column.tasks.findIndex((task) => task.id === taskId);

      try {
        await axios.delete(`http://localhost:7272/api/columns/task/${taskId}`);
        column.tasks.splice(taskIndex, 1);
        this.closeTaskPopup();
      } catch (error) {
        console.error('Error deleting task:', error);
      }
    },

    closeCreatePopup() {
      this.isCreatePopupActive = false;
    },

    closeTaskPopup() {
      this.isTaskPopupActive = false;
      this.selectedTask = null;
      this.selectedColumnIndex = null;
      this.updatedTask = {};
    },
  },
});

// Export the store type
export type KanbanStore = ReturnType<typeof useKanbanStore>;
