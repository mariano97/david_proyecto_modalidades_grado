<template>
  <div>
    <h2 id="page-heading" data-cy="ProfesorHeading">
      <span id="profesor-heading">Profesors</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'ProfesorCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-profesor"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Profesor </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && profesors && profesors.length === 0">
      <span>No profesors found</span>
    </div>
    <div class="table-responsive" v-if="profesors && profesors.length > 0">
      <table class="table table-striped" aria-describedby="profesors">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('primerNombre')">
              <span>Primer Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'primerNombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('segundoNombre')">
              <span>Segundo Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'segundoNombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('apellidos')">
              <span>Apellidos</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'apellidos'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('email')">
              <span>Email</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'email'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('telefono')">
              <span>Telefono</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'telefono'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="profesor in profesors" :key="profesor.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ProfesorView', params: { profesorId: profesor.id } }">{{ profesor.id }}</router-link>
            </td>
            <td>{{ profesor.primerNombre }}</td>
            <td>{{ profesor.segundoNombre }}</td>
            <td>{{ profesor.apellidos }}</td>
            <td>{{ profesor.email }}</td>
            <td>{{ profesor.telefono }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ProfesorView', params: { profesorId: profesor.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ProfesorEdit', params: { profesorId: profesor.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(profesor)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="paginaModalidadesGradoApp.profesor.delete.question" data-cy="profesorDeleteDialogHeading"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-profesor-heading">Are you sure you want to delete this Profesor?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-profesor"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeProfesor()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="profesors && profesors.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./profesor.component.ts"></script>
