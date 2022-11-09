<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="proyecto">
        <h2 class="jh-entity-heading" data-cy="proyectoDetailsHeading"><span>Proyecto</span> {{ proyecto.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ proyecto.nombre }}</span>
          </dd>
          <dt>
            <span>Acta</span>
          </dt>
          <dd>
            <span>{{ proyecto.acta }}</span>
          </dd>
          <dt>
            <span>Fecha Inicio</span>
          </dt>
          <dd>
            <span>{{ proyecto.fechaInicio | formatDate }}</span>
          </dd>
          <dt>
            <span>Fecha Termino</span>
          </dt>
          <dd>
            <span>{{ proyecto.fechaTermino | formatDate }}</span>
          </dd>
          <dt>
            <span>Tipo Modalidad</span>
          </dt>
          <dd>
            <div v-if="proyecto.tipoModalidad">
              <router-link :to="{ name: 'TablaContenidoView', params: { tablaContenidoId: proyecto.tipoModalidad.id } }">{{
                proyecto.tipoModalidad.nombre
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Empresa</span>
          </dt>
          <dd>
            <div v-if="proyecto.empresa">
              <router-link :to="{ name: 'EmpresaView', params: { empresaId: proyecto.empresa.id } }">{{
                proyecto.empresa.nombre
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Arl</span>
          </dt>
          <dd>
            <div v-if="proyecto.arl">
              <router-link :to="{ name: 'ArlView', params: { arlId: proyecto.arl.id } }">{{ proyecto.arl.nombre }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link v-if="proyecto.id" :to="{ name: 'ProyectoEdit', params: { proyectoId: proyecto.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./proyecto-details.component.ts"></script>
