<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="paginaModalidadesGradoApp.tablaMaestra.home.createOrEditLabel" data-cy="TablaMaestraCreateUpdateHeading">
          Create or edit a TablaMaestra
        </h2>
        <div>
          <div class="form-group" v-if="tablaMaestra.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="tablaMaestra.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="tabla-maestra-nombre">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="tabla-maestra-nombre"
              data-cy="nombre"
              :class="{ valid: !$v.tablaMaestra.nombre.$invalid, invalid: $v.tablaMaestra.nombre.$invalid }"
              v-model="$v.tablaMaestra.nombre.$model"
              required
            />
            <div v-if="$v.tablaMaestra.nombre.$anyDirty && $v.tablaMaestra.nombre.$invalid">
              <small class="form-text text-danger" v-if="!$v.tablaMaestra.nombre.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="tabla-maestra-codigo">Codigo</label>
            <input
              type="text"
              class="form-control"
              name="codigo"
              id="tabla-maestra-codigo"
              data-cy="codigo"
              :class="{ valid: !$v.tablaMaestra.codigo.$invalid, invalid: $v.tablaMaestra.codigo.$invalid }"
              v-model="$v.tablaMaestra.codigo.$model"
              required
            />
            <div v-if="$v.tablaMaestra.codigo.$anyDirty && $v.tablaMaestra.codigo.$invalid">
              <small class="form-text text-danger" v-if="!$v.tablaMaestra.codigo.required"> This field is required. </small>
            </div>
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
            :disabled="$v.tablaMaestra.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./tabla-maestra-update.component.ts"></script>
