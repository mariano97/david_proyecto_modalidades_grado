<template>
  <div>
    <h2 id="page-heading" data-cy="ProyectoHeading">
      <span id="proyecto-heading">Proyectos</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'ProyectoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-proyecto"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Proyecto </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && proyectos && proyectos.length === 0">
      <span>No proyectos found</span>
    </div>
    <div class="table-responsive" v-if="proyectos && proyectos.length > 0">
      <table class="table table-striped" aria-describedby="proyectos">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('nombre')">
              <span>Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('acta')">
              <span>Acta</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'acta'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('fechaInicio')">
              <span>Fecha Inicio</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaInicio'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('fechaTermino')">
              <span>Fecha Termino</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaTermino'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('tipoModalidad.nombre')">
              <span>Tipo Modalidad</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'tipoModalidad.nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('empresa.nombre')">
              <span>Empresa</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'empresa.nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('arl.nombre')">
              <span>Arl</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'arl.nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="proyecto in proyectos" :key="proyecto.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ProyectoView', params: { proyectoId: proyecto.id } }">{{ proyecto.id }}</router-link>
            </td>
            <td>{{ proyecto.nombre }}</td>
            <td>{{ proyecto.acta }}</td>
            <td>{{ proyecto.fechaInicio | formatDate }}</td>
            <td>{{ proyecto.fechaTermino | formatDate }}</td>
            <td>
              <div v-if="proyecto.tipoModalidad">
                <router-link :to="{ name: 'TablaContenidoView', params: { tablaContenidoId: proyecto.tipoModalidad.id } }">{{
                  proyecto.tipoModalidad.nombre
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="proyecto.empresa">
                <router-link :to="{ name: 'EmpresaView', params: { empresaId: proyecto.empresa.id } }">{{
                  proyecto.empresa.nombre
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="proyecto.arl">
                <router-link :to="{ name: 'ArlView', params: { arlId: proyecto.arl.id } }">{{ proyecto.arl.nombre }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ProyectoView', params: { proyectoId: proyecto.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ProyectoEdit', params: { proyectoId: proyecto.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(proyecto)"
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
        ><span id="paginaModalidadesGradoApp.proyecto.delete.question" data-cy="proyectoDeleteDialogHeading"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-proyecto-heading">Are you sure you want to delete this Proyecto?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-proyecto"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeProyecto()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./proyecto.component.ts"></script>
