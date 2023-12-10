<template>
  <div class="home d-flex align-items-center">
    <div class="card-custom jhi-card row p-3 m-4 w-100">
      <div class="col-md logo_container d-flex justify-content-center align-items-center">
        <!--<span class="hipster img-fluid rounded"></span>-->
        <img alt="logo" src="/content/images/imagenes/ingenieria.png" class="" />
      </div>
      <div class="col-md d-flex justify-content-center align-items-center container_ingreso">
        <div class="px-3 w-100" v-if="!authenticated">
          <login-form-template></login-form-template>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./home.component.ts"></script>
