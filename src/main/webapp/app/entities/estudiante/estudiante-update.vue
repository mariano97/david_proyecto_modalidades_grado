<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="paginaModalidadesGradoApp.estudiante.home.createOrEditLabel" data-cy="EstudianteCreateUpdateHeading">
          Create or edit a Estudiante
        </h2>
        <div>
          <div class="form-group" v-if="estudiante.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="estudiante.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="estudiante-primerNombre">Primer Nombre</label>
            <input
              type="text"
              class="form-control"
              name="primerNombre"
              id="estudiante-primerNombre"
              data-cy="primerNombre"
              :class="{ valid: !$v.estudiante.primerNombre.$invalid, invalid: $v.estudiante.primerNombre.$invalid }"
              v-model="$v.estudiante.primerNombre.$model"
              required
            />
            <div v-if="$v.estudiante.primerNombre.$anyDirty && $v.estudiante.primerNombre.$invalid">
              <small class="form-text text-danger" v-if="!$v.estudiante.primerNombre.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="estudiante-segundoNombre">Segundo Nombre</label>
            <input
              type="text"
              class="form-control"
              name="segundoNombre"
              id="estudiante-segundoNombre"
              data-cy="segundoNombre"
              :class="{ valid: !$v.estudiante.segundoNombre.$invalid, invalid: $v.estudiante.segundoNombre.$invalid }"
              v-model="$v.estudiante.segundoNombre.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="estudiante-apellidos">Apellidos</label>
            <input
              type="text"
              class="form-control"
              name="apellidos"
              id="estudiante-apellidos"
              data-cy="apellidos"
              :class="{ valid: !$v.estudiante.apellidos.$invalid, invalid: $v.estudiante.apellidos.$invalid }"
              v-model="$v.estudiante.apellidos.$model"
              required
            />
            <div v-if="$v.estudiante.apellidos.$anyDirty && $v.estudiante.apellidos.$invalid">
              <small class="form-text text-danger" v-if="!$v.estudiante.apellidos.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="estudiante-numeroDocumento">Numero Documento</label>
            <input
              type="text"
              class="form-control"
              name="numeroDocumento"
              id="estudiante-numeroDocumento"
              data-cy="numeroDocumento"
              :class="{ valid: !$v.estudiante.numeroDocumento.$invalid, invalid: $v.estudiante.numeroDocumento.$invalid }"
              v-model="$v.estudiante.numeroDocumento.$model"
              required
            />
            <div v-if="$v.estudiante.numeroDocumento.$anyDirty && $v.estudiante.numeroDocumento.$invalid">
              <small class="form-text text-danger" v-if="!$v.estudiante.numeroDocumento.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="estudiante-codigoEstudiantil">Codigo Estudiantil</label>
            <input
              type="text"
              class="form-control"
              name="codigoEstudiantil"
              id="estudiante-codigoEstudiantil"
              data-cy="codigoEstudiantil"
              :class="{ valid: !$v.estudiante.codigoEstudiantil.$invalid, invalid: $v.estudiante.codigoEstudiantil.$invalid }"
              v-model="$v.estudiante.codigoEstudiantil.$model"
              required
            />
            <div v-if="$v.estudiante.codigoEstudiantil.$anyDirty && $v.estudiante.codigoEstudiantil.$invalid">
              <small class="form-text text-danger" v-if="!$v.estudiante.codigoEstudiantil.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="estudiante-celular">Celular</label>
            <input
              type="text"
              class="form-control"
              name="celular"
              id="estudiante-celular"
              data-cy="celular"
              :class="{ valid: !$v.estudiante.celular.$invalid, invalid: $v.estudiante.celular.$invalid }"
              v-model="$v.estudiante.celular.$model"
              required
            />
            <div v-if="$v.estudiante.celular.$anyDirty && $v.estudiante.celular.$invalid">
              <small class="form-text text-danger" v-if="!$v.estudiante.celular.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="estudiante-email">Email</label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="estudiante-email"
              data-cy="email"
              :class="{ valid: !$v.estudiante.email.$invalid, invalid: $v.estudiante.email.$invalid }"
              v-model="$v.estudiante.email.$model"
              required
            />
            <div v-if="$v.estudiante.email.$anyDirty && $v.estudiante.email.$invalid">
              <small class="form-text text-danger" v-if="!$v.estudiante.email.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="estudiante-tipoDocumento">Tipo Documento</label>
            <select
              class="form-control"
              id="estudiante-tipoDocumento"
              data-cy="tipoDocumento"
              name="tipoDocumento"
              v-model="estudiante.tipoDocumento"
              required
            >
              <option v-if="!estudiante.tipoDocumento" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  estudiante.tipoDocumento && tablaContenidoOption.id === estudiante.tipoDocumento.id
                    ? estudiante.tipoDocumento
                    : tablaContenidoOption
                "
                v-for="tablaContenidoOption in tablaContenidos"
                :key="tablaContenidoOption.id"
              >
                {{ tablaContenidoOption.nombre }}
              </option>
            </select>
          </div>
          <div v-if="$v.estudiante.tipoDocumento.$anyDirty && $v.estudiante.tipoDocumento.$invalid">
            <small class="form-text text-danger" v-if="!$v.estudiante.tipoDocumento.required"> This field is required. </small>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="estudiante-proyecto">Proyecto</label>
            <select class="form-control" id="estudiante-proyecto" data-cy="proyecto" name="proyecto" v-model="estudiante.proyecto" required>
              <option v-if="!estudiante.proyecto" v-bind:value="null" selected></option>
              <option
                v-bind:value="estudiante.proyecto && proyectoOption.id === estudiante.proyecto.id ? estudiante.proyecto : proyectoOption"
                v-for="proyectoOption in proyectos"
                :key="proyectoOption.id"
              >
                {{ proyectoOption.nombre }}
              </option>
            </select>
          </div>
          <div v-if="$v.estudiante.proyecto.$anyDirty && $v.estudiante.proyecto.$invalid">
            <small class="form-text text-danger" v-if="!$v.estudiante.proyecto.required"> This field is required. </small>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.estudiante.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./estudiante-update.component.ts"></script>
