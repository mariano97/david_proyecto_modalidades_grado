<template>
  <div>
    <h2 id="page-heading" data-cy="ArlHeading">
      <span id="arl-heading">Arls</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'ArlCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-arl">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Arl </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && arls && arls.length === 0">
      <span>No arls found</span>
    </div>
    <div class="table-responsive" v-if="arls && arls.length > 0">
      <table class="table table-striped" aria-describedby="arls">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('nombre')">
              <span>Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="arl in arls" :key="arl.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ArlView', params: { arlId: arl.id } }">{{ arl.id }}</router-link>
            </td>
            <td>{{ arl.nombre }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ArlView', params: { arlId: arl.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ArlEdit', params: { arlId: arl.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(arl)"
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
        ><span id="paginaModalidadesGradoApp.arl.delete.question" data-cy="arlDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-arl-heading">Are you sure you want to delete this Arl?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-arl"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeArl()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="arls && arls.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./arl.component.ts"></script>
