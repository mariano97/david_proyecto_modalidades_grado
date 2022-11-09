<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="paginaModalidadesGradoApp.arl.home.createOrEditLabel" data-cy="ArlCreateUpdateHeading">Create or edit a Arl</h2>
        <div>
          <div class="form-group" v-if="arl.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="arl.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="arl-nombre">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="arl-nombre"
              data-cy="nombre"
              :class="{ valid: !$v.arl.nombre.$invalid, invalid: $v.arl.nombre.$invalid }"
              v-model="$v.arl.nombre.$model"
              required
            />
            <div v-if="$v.arl.nombre.$anyDirty && $v.arl.nombre.$invalid">
              <small class="form-text text-danger" v-if="!$v.arl.nombre.required"> This field is required. </small>
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
            :disabled="$v.arl.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./arl-update.component.ts"></script>
