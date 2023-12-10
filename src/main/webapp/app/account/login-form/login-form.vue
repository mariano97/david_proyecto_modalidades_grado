<template>
  <div class="modal-body">
    <div class="row justify-content-center">
      <div class="col-md-8">
        <b-alert show data-cy="loginError" variant="danger" v-if="authenticationError">
          <strong>Fallo en la autenticación</strong> Por favor verifique sus credenciales e intente nuevamente.
        </b-alert>
      </div>
      <div class="col-md-8">
        <b-form role="form" v-on:submit.prevent="doLogin()">
          <b-form-group label="Usuario" label-for="username">
            <b-form-input id="username" type="text" name="username" autofocus placeholder="Usuario" v-model="login" data-cy="username">
            </b-form-input>
          </b-form-group>
          <b-form-group label="Contraseña" label-for="password">
            <b-form-input
              id="password"
              type="password"
              name="password"
              v-model.trim="name"
              placeholder="Contrseña"
              v-model="password"
              data-cy="password"
            >
            </b-form-input>
          </b-form-group>
          <!--<b-form-checkbox id="rememberMe" name="rememberMe" v-model="rememberMe" checked>
            <span>Remember me</span>
          </b-form-checkbox>-->
          <div>
            <b-button data-cy="submit" type="submit" class="btn btn_primary w-100">Ingresar</b-button>
          </div>
        </b-form>
        <p></p>
        <div>
          <!---<b-alert show variant="warning">
            <b-link :to="'/account/reset/request'" class="alert-link" data-cy="forgetYourPasswordSelector"
              >Did you forget your password?</b-link
            >
          </b-alert>-->
        </div>
        <div>
          <!--<b-alert show variant="warning">
            <span>You don't have an account yet?</span>
            <b-link :to="'/register'" class="alert-link">Register a new account</b-link>
          </b-alert>-->
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./login-form.component.ts"></script>
