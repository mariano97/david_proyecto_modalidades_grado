<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="paginaModalidadesGradoApp.profesor.home.createOrEditLabel" data-cy="ProfesorCreateUpdateHeading">
          Create or edit a Profesor
        </h2>
        <div>
          <div class="form-group" v-if="profesor.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="profesor.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="profesor-primerNombre">Primer Nombre</label>
            <input
              type="text"
              class="form-control"
              name="primerNombre"
              id="profesor-primerNombre"
              data-cy="primerNombre"
              :class="{ valid: !$v.profesor.primerNombre.$invalid, invalid: $v.profesor.primerNombre.$invalid }"
              v-model="$v.profesor.primerNombre.$model"
              required
            />
            <div v-if="$v.profesor.primerNombre.$anyDirty && $v.profesor.primerNombre.$invalid">
              <small class="form-text text-danger" v-if="!$v.profesor.primerNombre.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="profesor-segundoNombre">Segundo Nombre</label>
            <input
              type="text"
              class="form-control"
              name="segundoNombre"
              id="profesor-segundoNombre"
              data-cy="segundoNombre"
              :class="{ valid: !$v.profesor.segundoNombre.$invalid, invalid: $v.profesor.segundoNombre.$invalid }"
              v-model="$v.profesor.segundoNombre.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="profesor-apellidos">Apellidos</label>
            <input
              type="text"
              class="form-control"
              name="apellidos"
              id="profesor-apellidos"
              data-cy="apellidos"
              :class="{ valid: !$v.profesor.apellidos.$invalid, invalid: $v.profesor.apellidos.$invalid }"
              v-model="$v.profesor.apellidos.$model"
              required
            />
            <div v-if="$v.profesor.apellidos.$anyDirty && $v.profesor.apellidos.$invalid">
              <small class="form-text text-danger" v-if="!$v.profesor.apellidos.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="profesor-email">Email</label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="profesor-email"
              data-cy="email"
              :class="{ valid: !$v.profesor.email.$invalid, invalid: $v.profesor.email.$invalid }"
              v-model="$v.profesor.email.$model"
              required
            />
            <div v-if="$v.profesor.email.$anyDirty && $v.profesor.email.$invalid">
              <small class="form-text text-danger" v-if="!$v.profesor.email.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="profesor-telefono">Telefono</label>
            <input
              type="text"
              class="form-control"
              name="telefono"
              id="profesor-telefono"
              data-cy="telefono"
              :class="{ valid: !$v.profesor.telefono.$invalid, invalid: $v.profesor.telefono.$invalid }"
              v-model="$v.profesor.telefono.$model"
            />
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
            :disabled="$v.profesor.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./profesor-update.component.ts"></script>
