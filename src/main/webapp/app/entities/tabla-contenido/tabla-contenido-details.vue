<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="tablaContenido">
        <h2 class="jh-entity-heading" data-cy="tablaContenidoDetailsHeading"><span>TablaContenido</span> {{ tablaContenido.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ tablaContenido.nombre }}</span>
          </dd>
          <dt>
            <span>Codigo</span>
          </dt>
          <dd>
            <span>{{ tablaContenido.codigo }}</span>
          </dd>
          <dt>
            <span>Tabla Maestra</span>
          </dt>
          <dd>
            <div v-if="tablaContenido.tablaMaestra">
              <router-link :to="{ name: 'TablaMaestraView', params: { tablaMaestraId: tablaContenido.tablaMaestra.id } }">{{
                tablaContenido.tablaMaestra.nombre
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="tablaContenido.id"
          :to="{ name: 'TablaContenidoEdit', params: { tablaContenidoId: tablaContenido.id } }"
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

<script lang="ts" src="./tabla-contenido-details.component.ts"></script>
