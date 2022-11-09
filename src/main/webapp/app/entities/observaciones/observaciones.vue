<template>
  <div>
    <h2 id="page-heading" data-cy="ObservacionesHeading">
      <span id="observaciones-heading">Observaciones</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'ObservacionesCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-observaciones"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Observaciones </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && observaciones && observaciones.length === 0">
      <span>No observaciones found</span>
    </div>
    <div class="table-responsive" v-if="observaciones && observaciones.length > 0">
      <table class="table table-striped" aria-describedby="observaciones">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('observacion')">
              <span>Observacion</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'observacion'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('proyecto.nombre')">
              <span>Proyecto</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'proyecto.nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="observaciones in observaciones" :key="observaciones.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ObservacionesView', params: { observacionesId: observaciones.id } }">{{
                observaciones.id
              }}</router-link>
            </td>
            <td>{{ observaciones.observacion }}</td>
            <td>
              <div v-if="observaciones.proyecto">
                <router-link :to="{ name: 'ProyectoView', params: { proyectoId: observaciones.proyecto.id } }">{{
                  observaciones.proyecto.nombre
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ObservacionesView', params: { observacionesId: observaciones.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ObservacionesEdit', params: { observacionesId: observaciones.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(observaciones)"
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
        <infinite-loading
          ref="infiniteLoading"
          v-if="totalItems > itemsPerPage"
          :identifier="infiniteId"
          slot="append"
          @infinite="loadMore"
          force-use-infinite-wrapper=".el-table__body-wrapper"
          :distance="20"
        >
        </infinite-loading>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="paginaModalidadesGradoApp.observaciones.delete.question" data-cy="observacionesDeleteDialogHeading"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-observaciones-heading">Are you sure you want to delete this Observaciones?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-observaciones"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeObservaciones()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./observaciones.component.ts"></script>
