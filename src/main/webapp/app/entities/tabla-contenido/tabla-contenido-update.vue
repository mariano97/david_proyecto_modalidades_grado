<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="paginaModalidadesGradoApp.tablaContenido.home.createOrEditLabel" data-cy="TablaContenidoCreateUpdateHeading">
          Create or edit a TablaContenido
        </h2>
        <div>
          <div class="form-group" v-if="tablaContenido.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="tablaContenido.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="tabla-contenido-nombre">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="tabla-contenido-nombre"
              data-cy="nombre"
              :class="{ valid: !$v.tablaContenido.nombre.$invalid, invalid: $v.tablaContenido.nombre.$invalid }"
              v-model="$v.tablaContenido.nombre.$model"
              required
            />
            <div v-if="$v.tablaContenido.nombre.$anyDirty && $v.tablaContenido.nombre.$invalid">
              <small class="form-text text-danger" v-if="!$v.tablaContenido.nombre.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="tabla-contenido-codigo">Codigo</label>
            <input
              type="text"
              class="form-control"
              name="codigo"
              id="tabla-contenido-codigo"
              data-cy="codigo"
              :class="{ valid: !$v.tablaContenido.codigo.$invalid, invalid: $v.tablaContenido.codigo.$invalid }"
              v-model="$v.tablaContenido.codigo.$model"
              required
            />
            <div v-if="$v.tablaContenido.codigo.$anyDirty && $v.tablaContenido.codigo.$invalid">
              <small class="form-text text-danger" v-if="!$v.tablaContenido.codigo.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="tabla-contenido-tablaMaestra">Tabla Maestra</label>
            <select
              class="form-control"
              id="tabla-contenido-tablaMaestra"
              data-cy="tablaMaestra"
              name="tablaMaestra"
              v-model="tablaContenido.tablaMaestra"
              required
            >
              <option v-if="!tablaContenido.tablaMaestra" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  tablaContenido.tablaMaestra && tablaMaestraOption.id === tablaContenido.tablaMaestra.id
                    ? tablaContenido.tablaMaestra
                    : tablaMaestraOption
                "
                v-for="tablaMaestraOption in tablaMaestras"
                :key="tablaMaestraOption.id"
              >
                {{ tablaMaestraOption.nombre }}
              </option>
            </select>
          </div>
          <div v-if="$v.tablaContenido.tablaMaestra.$anyDirty && $v.tablaContenido.tablaMaestra.$invalid">
            <small class="form-text text-danger" v-if="!$v.tablaContenido.tablaMaestra.required"> This field is required. </small>
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
            :disabled="$v.tablaContenido.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./tabla-contenido-update.component.ts"></script>
