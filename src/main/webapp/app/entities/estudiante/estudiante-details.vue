<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="estudiante">
        <h2 class="jh-entity-heading" data-cy="estudianteDetailsHeading"><span>Estudiante</span> {{ estudiante.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Primer Nombre</span>
          </dt>
          <dd>
            <span>{{ estudiante.primerNombre }}</span>
          </dd>
          <dt>
            <span>Segundo Nombre</span>
          </dt>
          <dd>
            <span>{{ estudiante.segundoNombre }}</span>
          </dd>
          <dt>
            <span>Apellidos</span>
          </dt>
          <dd>
            <span>{{ estudiante.apellidos }}</span>
          </dd>
          <dt>
            <span>Numero Documento</span>
          </dt>
          <dd>
            <span>{{ estudiante.numeroDocumento }}</span>
          </dd>
          <dt>
            <span>Codigo Estudiantil</span>
          </dt>
          <dd>
            <span>{{ estudiante.codigoEstudiantil }}</span>
          </dd>
          <dt>
            <span>Celular</span>
          </dt>
          <dd>
            <span>{{ estudiante.celular }}</span>
          </dd>
          <dt>
            <span>Email</span>
          </dt>
          <dd>
            <span>{{ estudiante.email }}</span>
          </dd>
          <dt>
            <span>Tipo Documento</span>
          </dt>
          <dd>
            <div v-if="estudiante.tipoDocumento">
              <router-link :to="{ name: 'TablaContenidoView', params: { tablaContenidoId: estudiante.tipoDocumento.id } }">{{
                estudiante.tipoDocumento.nombre
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Proyecto</span>
          </dt>
          <dd>
            <div v-if="estudiante.proyecto">
              <router-link :to="{ name: 'ProyectoView', params: { proyectoId: estudiante.proyecto.id } }">{{
                estudiante.proyecto.nombre
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="estudiante.id"
          :to="{ name: 'EstudianteEdit', params: { estudianteId: estudiante.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./estudiante-details.component.ts"></script>
