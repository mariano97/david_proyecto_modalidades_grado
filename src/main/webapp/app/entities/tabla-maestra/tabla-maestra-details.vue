<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="tablaMaestra">
        <h2 class="jh-entity-heading" data-cy="tablaMaestraDetailsHeading"><span>TablaMaestra</span> {{ tablaMaestra.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ tablaMaestra.nombre }}</span>
          </dd>
          <dt>
            <span>Codigo</span>
          </dt>
          <dd>
            <span>{{ tablaMaestra.codigo }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="tablaMaestra.id"
          :to="{ name: 'TablaMaestraEdit', params: { tablaMaestraId: tablaMaestra.id } }"
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

<script lang="ts" src="./tabla-maestra-details.component.ts"></script>
