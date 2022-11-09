<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="paginaModalidadesGradoApp.empresa.home.createOrEditLabel" data-cy="EmpresaCreateUpdateHeading">Create or edit a Empresa</h2>
        <div>
          <div class="form-group" v-if="empresa.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="empresa.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="empresa-nombre">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="empresa-nombre"
              data-cy="nombre"
              :class="{ valid: !$v.empresa.nombre.$invalid, invalid: $v.empresa.nombre.$invalid }"
              v-model="$v.empresa.nombre.$model"
              required
            />
            <div v-if="$v.empresa.nombre.$anyDirty && $v.empresa.nombre.$invalid">
              <small class="form-text text-danger" v-if="!$v.empresa.nombre.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="empresa-nit">Nit</label>
            <input
              type="text"
              class="form-control"
              name="nit"
              id="empresa-nit"
              data-cy="nit"
              :class="{ valid: !$v.empresa.nit.$invalid, invalid: $v.empresa.nit.$invalid }"
              v-model="$v.empresa.nit.$model"
              required
            />
            <div v-if="$v.empresa.nit.$anyDirty && $v.empresa.nit.$invalid">
              <small class="form-text text-danger" v-if="!$v.empresa.nit.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="empresa-telefono">Telefono</label>
            <input
              type="text"
              class="form-control"
              name="telefono"
              id="empresa-telefono"
              data-cy="telefono"
              :class="{ valid: !$v.empresa.telefono.$invalid, invalid: $v.empresa.telefono.$invalid }"
              v-model="$v.empresa.telefono.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="empresa-email">Email</label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="empresa-email"
              data-cy="email"
              :class="{ valid: !$v.empresa.email.$invalid, invalid: $v.empresa.email.$invalid }"
              v-model="$v.empresa.email.$model"
              required
            />
            <div v-if="$v.empresa.email.$anyDirty && $v.empresa.email.$invalid">
              <small class="form-text text-danger" v-if="!$v.empresa.email.required"> This field is required. </small>
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
            :disabled="$v.empresa.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./empresa-update.component.ts"></script>
