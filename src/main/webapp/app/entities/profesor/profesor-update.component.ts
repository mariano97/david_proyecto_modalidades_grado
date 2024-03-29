import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import { IProfesor, Profesor } from '@/shared/model/profesor.model';
import ProfesorService from './profesor.service';

const validations: any = {
  profesor: {
    primerNombre: {
      required,
    },
    segundoNombre: {},
    apellidos: {
      required,
    },
    email: {
      required,
    },
    telefono: {},
  },
};

@Component({
  validations,
})
export default class ProfesorUpdate extends Vue {
  @Inject('profesorService') private profesorService: () => ProfesorService;
  @Inject('alertService') private alertService: () => AlertService;

  public profesor: IProfesor = new Profesor();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.profesorId) {
        vm.retrieveProfesor(to.params.profesorId);
      }
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.profesor.id) {
      this.profesorService()
        .update(this.profesor)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Profesor is updated with identifier ' + param.id;
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.profesorService()
        .create(this.profesor)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Profesor is created with identifier ' + param.id;
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrieveProfesor(profesorId): void {
    this.profesorService()
      .find(profesorId)
      .then(res => {
        this.profesor = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
