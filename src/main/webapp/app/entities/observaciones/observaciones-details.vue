<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="observaciones">
        <h2 class="jh-entity-heading" data-cy="observacionesDetailsHeading"><span>Observaciones</span> {{ observaciones.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Observacion</span>
          </dt>
          <dd>
            <span>{{ observaciones.observacion }}</span>
          </dd>
          <dt>
            <span>Proyecto</span>
          </dt>
          <dd>
            <div v-if="observaciones.proyecto">
              <router-link :to="{ name: 'ProyectoView', params: { proyectoId: observaciones.proyecto.id } }">{{
                observaciones.proyecto.nombre
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="observaciones.id"
          :to="{ name: 'ObservacionesEdit', params: { observacionesId: observaciones.id } }"
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

<script lang="ts" src="./observaciones-details.component.ts"></script>
