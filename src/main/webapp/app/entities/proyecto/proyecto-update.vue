<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="paginaModalidadesGradoApp.proyecto.home.createOrEditLabel" data-cy="ProyectoCreateUpdateHeading">
          Create or edit a Proyecto
        </h2>
        <div>
          <div class="form-group" v-if="proyecto.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="proyecto.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="proyecto-nombre">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="proyecto-nombre"
              data-cy="nombre"
              :class="{ valid: !$v.proyecto.nombre.$invalid, invalid: $v.proyecto.nombre.$invalid }"
              v-model="$v.proyecto.nombre.$model"
              required
            />
            <div v-if="$v.proyecto.nombre.$anyDirty && $v.proyecto.nombre.$invalid">
              <small class="form-text text-danger" v-if="!$v.proyecto.nombre.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="proyecto-acta">Acta</label>
            <input
              type="checkbox"
              class="form-check"
              name="acta"
              id="proyecto-acta"
              data-cy="acta"
              :class="{ valid: !$v.proyecto.acta.$invalid, invalid: $v.proyecto.acta.$invalid }"
              v-model="$v.proyecto.acta.$model"
              required
            />
            <div v-if="$v.proyecto.acta.$anyDirty && $v.proyecto.acta.$invalid">
              <small class="form-text text-danger" v-if="!$v.proyecto.acta.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="proyecto-fechaInicio">Fecha Inicio</label>
            <div class="d-flex">
              <input
                id="proyecto-fechaInicio"
                data-cy="fechaInicio"
                type="datetime-local"
                class="form-control"
                name="fechaInicio"
                :class="{ valid: !$v.proyecto.fechaInicio.$invalid, invalid: $v.proyecto.fechaInicio.$invalid }"
                required
                :value="convertDateTimeFromServer($v.proyecto.fechaInicio.$model)"
                @change="updateInstantField('fechaInicio', $event)"
              />
            </div>
            <div v-if="$v.proyecto.fechaInicio.$anyDirty && $v.proyecto.fechaInicio.$invalid">
              <small class="form-text text-danger" v-if="!$v.proyecto.fechaInicio.required"> This field is required. </small>
              <small class="form-text text-danger" v-if="!$v.proyecto.fechaInicio.ZonedDateTimelocal">
                This field should be a date and time.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="proyecto-fechaTermino">Fecha Termino</label>
            <div class="d-flex">
              <input
                id="proyecto-fechaTermino"
                data-cy="fechaTermino"
                type="datetime-local"
                class="form-control"
                name="fechaTermino"
                :class="{ valid: !$v.proyecto.fechaTermino.$invalid, invalid: $v.proyecto.fechaTermino.$invalid }"
                :value="convertDateTimeFromServer($v.proyecto.fechaTermino.$model)"
                @change="updateInstantField('fechaTermino', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="proyecto-tipoModalidad">Tipo Modalidad</label>
            <select
              class="form-control"
              id="proyecto-tipoModalidad"
              data-cy="tipoModalidad"
              name="tipoModalidad"
              v-model="proyecto.tipoModalidad"
              required
            >
              <option v-if="!proyecto.tipoModalidad" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  proyecto.tipoModalidad && tablaContenidoOption.id === proyecto.tipoModalidad.id
                    ? proyecto.tipoModalidad
                    : tablaContenidoOption
                "
                v-for="tablaContenidoOption in tablaContenidos"
                :key="tablaContenidoOption.id"
              >
                {{ tablaContenidoOption.nombre }}
              </option>
            </select>
          </div>
          <div v-if="$v.proyecto.tipoModalidad.$anyDirty && $v.proyecto.tipoModalidad.$invalid">
            <small class="form-text text-danger" v-if="!$v.proyecto.tipoModalidad.required"> This field is required. </small>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="proyecto-empresa">Empresa</label>
            <select class="form-control" id="proyecto-empresa" data-cy="empresa" name="empresa" v-model="proyecto.empresa">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="proyecto.empresa && empresaOption.id === proyecto.empresa.id ? proyecto.empresa : empresaOption"
                v-for="empresaOption in empresas"
                :key="empresaOption.id"
              >
                {{ empresaOption.nombre }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="proyecto-arl">Arl</label>
            <select class="form-control" id="proyecto-arl" data-cy="arl" name="arl" v-model="proyecto.arl">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="proyecto.arl && arlOption.id === proyecto.arl.id ? proyecto.arl : arlOption"
                v-for="arlOption in arls"
                :key="arlOption.id"
              >
                {{ arlOption.nombre }}
              </option>
            </select>
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
            :disabled="$v.proyecto.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./proyecto-update.component.ts"></script>
