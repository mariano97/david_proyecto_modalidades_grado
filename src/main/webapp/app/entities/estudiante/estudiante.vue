<template>
  <div>
    <h2 id="page-heading" data-cy="EstudianteHeading">
      <span id="estudiante-heading">Estudiantes</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'EstudianteCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-estudiante"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Estudiante </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && estudiantes && estudiantes.length === 0">
      <span>No estudiantes found</span>
    </div>
    <div class="table-responsive" v-if="estudiantes && estudiantes.length > 0">
      <table class="table table-striped" aria-describedby="estudiantes">
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
            <th scope="row" v-on:click="changeOrder('numeroDocumento')">
              <span>Numero Documento</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'numeroDocumento'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('codigoEstudiantil')">
              <span>Codigo Estudiantil</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'codigoEstudiantil'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('celular')">
              <span>Celular</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'celular'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('email')">
              <span>Email</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'email'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('tipoDocumento.nombre')">
              <span>Tipo Documento</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'tipoDocumento.nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('proyecto.nombre')">
              <span>Proyecto</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'proyecto.nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="estudiante in estudiantes" :key="estudiante.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'EstudianteView', params: { estudianteId: estudiante.id } }">{{ estudiante.id }}</router-link>
            </td>
            <td>{{ estudiante.primerNombre }}</td>
            <td>{{ estudiante.segundoNombre }}</td>
            <td>{{ estudiante.apellidos }}</td>
            <td>{{ estudiante.numeroDocumento }}</td>
            <td>{{ estudiante.codigoEstudiantil }}</td>
            <td>{{ estudiante.celular }}</td>
            <td>{{ estudiante.email }}</td>
            <td>
              <div v-if="estudiante.tipoDocumento">
                <router-link :to="{ name: 'TablaContenidoView', params: { tablaContenidoId: estudiante.tipoDocumento.id } }">{{
                  estudiante.tipoDocumento.nombre
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="estudiante.proyecto">
                <router-link :to="{ name: 'ProyectoView', params: { proyectoId: estudiante.proyecto.id } }">{{
                  estudiante.proyecto.nombre
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'EstudianteView', params: { estudianteId: estudiante.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'EstudianteEdit', params: { estudianteId: estudiante.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(estudiante)"
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
        ><span id="paginaModalidadesGradoApp.estudiante.delete.question" data-cy="estudianteDeleteDialogHeading"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-estudiante-heading">Are you sure you want to delete this Estudiante?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-estudiante"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeEstudiante()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="estudiantes && estudiantes.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./estudiante.component.ts"></script>
