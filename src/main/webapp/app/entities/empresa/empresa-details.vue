<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="empresa">
        <h2 class="jh-entity-heading" data-cy="empresaDetailsHeading"><span>Empresa</span> {{ empresa.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ empresa.nombre }}</span>
          </dd>
          <dt>
            <span>Nit</span>
          </dt>
          <dd>
            <span>{{ empresa.nit }}</span>
          </dd>
          <dt>
            <span>Telefono</span>
          </dt>
          <dd>
            <span>{{ empresa.telefono }}</span>
          </dd>
          <dt>
            <span>Email</span>
          </dt>
          <dd>
            <span>{{ empresa.email }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link v-if="empresa.id" :to="{ name: 'EmpresaEdit', params: { empresaId: empresa.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./empresa-details.component.ts"></script>
