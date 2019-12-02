<template>
  <q-layout view="lHh Lpr lFf">
    <q-header elevated>
      <q-toolbar class="toolbar-style">
        <q-toolbar-title class="row">
          <img src="statics/redhat-logo.png" class="logo"/>
          <span class="col-grow q-my-auto">Quarkus Todo</span>
          <q-btn fab-mini :icon="formIcon" @click="toggleTodoForm"/>
        </q-toolbar-title>
      </q-toolbar>
    </q-header>
    <q-page-container>
      <div class="q-pa-y items-start row" v-if="showForm">
        <div class="q-px-sm col">
          <q-input type="text" label="Title" v-model="newTodo.title" @keyup.enter="addTodo"
                   class="row"/>
          <q-input type="textarea" style="height: 6em;" @keyup.enter="addTodo" label="Description"
                   v-model="newTodo.description" class="row"/>
          <q-input filled v-model="newTodo.dueDate" class="row">
            <template v-slot:prepend>
              <q-icon name="event" class="cursor-pointer">
                <q-popup-proxy transition-show="scale" transition-hide="scale">
                  <q-date v-model="newTodo.dueDate" :mask="dateMask"/>
                </q-popup-proxy>
              </q-icon>
            </template>

            <template v-slot:append>
              <q-icon name="access_time" class="cursor-pointer">
                <q-popup-proxy transition-show="scale" transition-hide="scale">
                  <q-time v-model="newTodo.dueDate" :mask="dateMask"/>
                </q-popup-proxy>
              </q-icon>
            </template>
          </q-input>
          <q-btn icon="add" label="Add" dense @click="addTodo" type="submit" class="row"/>
        </div>
      </div>
      <div class="q-px-sm items-start row" v-if="!showForm">
        <div class="col">
          <q-card v-for="(todo, index) in todos" :key="todo.id" style="width: 100%;" bordered
                  class="q-pa-none q-my-sm row">
            <div class="col">
              <q-card-section class="title-section" :class="{ completed: todo.complete }">
                <q-input borderless @change="updateTodo(index)" v-model="todos[index].title" :input-style="{ fontSize: '1.6em', fontWeight: 'bold' }"></q-input>
              </q-card-section>
              <q-card-section class="row">
                <q-input borderless @change="updateTodo(index)" :class="{ completed: todo.complete }" v-model="todos[index].description" class="col-grow q-mr-xl"></q-input>
                <q-checkbox dense @input="updateTodo(index)" v-model="todos[index].complete"
                            class="col-1 q-ml-xl"/>
              </q-card-section>
            </div>
          </q-card>
        </div>
      </div>
    </q-page-container>
  </q-layout>
</template>

<style>
  .title-section {
    font-size: 2.5em;
  }

  .completed {
    opacity: 0.3;
  }

  .toolbar-style {
    background-color: rgb(0, 0, 0);
  }

  .logo {
    max-height: 1.6em;
    align-content: center;
    padding-right: 0.2em;
  }
</style>

<script>
import { DefaultApi } from 'todo-api-client';

const apiBaseUrl = window.location.protocol + '//' + window.location.host;
// const apiBaseUrl = window.location.protocol + '//localhost:8080';
const apiClient = new DefaultApi(null, apiBaseUrl);

export default {
  name: 'MyLayout',

  data() {
    return {
      newTodo: {title: '', description: '', complete: false, dueDate: null},
      todos: [],
      showForm: false,
      dateMask: 'YYYY-MM-DDTHH:mmZ'
    };
  },
  mounted() {
    // When first starting, the application needs to load the initial data from the
    // server. This uses the loading indicator and the generated Axios client SDK
    // to load the data while showing the user a loading indicator
    this.$q.loading.show();
    apiClient.gettodos()
      .then(res => {
        this.$data.todos = res.data;
        this.$q.loading.hide();
      }).catch(err => {
        this.$q.loading.hide();
        this.$q.notify('An error occurred loading data from the server: ' + err);
      })
  },
  methods: {
    // Whenever a change is made to the data, this method is run to updated the
    // persisted data on the server
    updateTodo: function(index) {
      const updated = this.$data.todos[index];
      apiClient.updateTodo(updated.id, updated)
        .catch(err => {
          this.$q.notify('An error occurred updating the todo: ' + err);
        });
    },
    // When a new ToDo is submitted, this method is called to persist the ToDo
    // object to the server
    addTodo: function () {
      apiClient.createTodo(this.$data.newTodo)
        .then(res => {
          this.$data.todos.push(res.data);
          this.$data.newTodo = {title: '', description: '', complete: false, dueDate: null};
          this.$data.showForm = false;
        })
        .catch(err => {
          this.$q.notify('An error occurred saving the todo: ' + err);
        })
    },
    // A method to show or hide the new ToDo form
    toggleTodoForm: function () {
      this.$data.showForm = !this.$data.showForm;
    }
  },
  computed: {
    // The computed icon to be shown in the top-right of the UI depending on if
    // the user is viewing the form or the list
    formIcon: function () {
      return this.$data.showForm ? 'chevron_left' : 'add';
    }
  }
};
</script>
