<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="profesor">
        <h2 class="jh-entity-heading" data-cy="profesorDetailsHeading"><span>Profesor</span> {{ profesor.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Primer Nombre</span>
          </dt>
          <dd>
            <span>{{ profesor.primerNombre }}</span>
          </dd>
          <dt>
            <span>Segundo Nombre</span>
          </dt>
          <dd>
            <span>{{ profesor.segundoNombre }}</span>
          </dd>
          <dt>
            <span>Apellidos</span>
          </dt>
          <dd>
            <span>{{ profesor.apellidos }}</span>
          </dd>
          <dt>
            <span>Email</span>
          </dt>
          <dd>
            <span>{{ profesor.email }}</span>
          </dd>
          <dt>
            <span>Telefono</span>
          </dt>
          <dd>
            <span>{{ profesor.telefono }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link v-if="profesor.id" :to="{ name: 'ProfesorEdit', params: { profesorId: profesor.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./profesor-details.component.ts"></script>
