<template>
  <div>
    <h2 id="page-heading" data-cy="TablaContenidoHeading">
      <span id="tabla-contenido-heading">Tabla Contenidos</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'TablaContenidoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-tabla-contenido"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Tabla Contenido </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && tablaContenidos && tablaContenidos.length === 0">
      <span>No tablaContenidos found</span>
    </div>
    <div class="table-responsive" v-if="tablaContenidos && tablaContenidos.length > 0">
      <table class="table table-striped" aria-describedby="tablaContenidos">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('nombre')">
              <span>Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('codigo')">
              <span>Codigo</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'codigo'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('tablaMaestra.nombre')">
              <span>Tabla Maestra</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'tablaMaestra.nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="tablaContenido in tablaContenidos" :key="tablaContenido.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'TablaContenidoView', params: { tablaContenidoId: tablaContenido.id } }">{{
                tablaContenido.id
              }}</router-link>
            </td>
            <td>{{ tablaContenido.nombre }}</td>
            <td>{{ tablaContenido.codigo }}</td>
            <td>
              <div v-if="tablaContenido.tablaMaestra">
                <router-link :to="{ name: 'TablaMaestraView', params: { tablaMaestraId: tablaContenido.tablaMaestra.id } }">{{
                  tablaContenido.tablaMaestra.nombre
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'TablaContenidoView', params: { tablaContenidoId: tablaContenido.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'TablaContenidoEdit', params: { tablaContenidoId: tablaContenido.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(tablaContenido)"
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
        ><span id="paginaModalidadesGradoApp.tablaContenido.delete.question" data-cy="tablaContenidoDeleteDialogHeading"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-tablaContenido-heading">Are you sure you want to delete this Tabla Contenido?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-tablaContenido"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeTablaContenido()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="tablaContenidos && tablaContenidos.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./tabla-contenido.component.ts"></script>
