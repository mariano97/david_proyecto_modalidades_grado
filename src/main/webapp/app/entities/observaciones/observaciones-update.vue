<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="paginaModalidadesGradoApp.observaciones.home.createOrEditLabel" data-cy="ObservacionesCreateUpdateHeading">
          Create or edit a Observaciones
        </h2>
        <div>
          <div class="form-group" v-if="observaciones.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="observaciones.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="observaciones-observacion">Observacion</label>
            <input
              type="text"
              class="form-control"
              name="observacion"
              id="observaciones-observacion"
              data-cy="observacion"
              :class="{ valid: !$v.observaciones.observacion.$invalid, invalid: $v.observaciones.observacion.$invalid }"
              v-model="$v.observaciones.observacion.$model"
              required
            />
            <div v-if="$v.observaciones.observacion.$anyDirty && $v.observaciones.observacion.$invalid">
              <small class="form-text text-danger" v-if="!$v.observaciones.observacion.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="observaciones-proyecto">Proyecto</label>
            <select
              class="form-control"
              id="observaciones-proyecto"
              data-cy="proyecto"
              name="proyecto"
              v-model="observaciones.proyecto"
              required
            >
              <option v-if="!observaciones.proyecto" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  observaciones.proyecto && proyectoOption.id === observaciones.proyecto.id ? observaciones.proyecto : proyectoOption
                "
                v-for="proyectoOption in proyectos"
                :key="proyectoOption.id"
              >
                {{ proyectoOption.nombre }}
              </option>
            </select>
          </div>
          <div v-if="$v.observaciones.proyecto.$anyDirty && $v.observaciones.proyecto.$invalid">
            <small class="form-text text-danger" v-if="!$v.observaciones.proyecto.required"> This field is required. </small>
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
            :disabled="$v.observaciones.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./observaciones-update.component.ts"></script>
