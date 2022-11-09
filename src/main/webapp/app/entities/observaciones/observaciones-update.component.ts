import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import ProyectoService from '@/entities/proyecto/proyecto.service';
import { IProyecto } from '@/shared/model/proyecto.model';

import { IObservaciones, Observaciones } from '@/shared/model/observaciones.model';
import ObservacionesService from './observaciones.service';

const validations: any = {
  observaciones: {
    observacion: {
      required,
    },
    proyecto: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class ObservacionesUpdate extends Vue {
  @Inject('observacionesService') private observacionesService: () => ObservacionesService;
  @Inject('alertService') private alertService: () => AlertService;

  public observaciones: IObservaciones = new Observaciones();

  @Inject('proyectoService') private proyectoService: () => ProyectoService;

  public proyectos: IProyecto[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.observacionesId) {
        vm.retrieveObservaciones(to.params.observacionesId);
      }
      vm.initRelationships();
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
    if (this.observaciones.id) {
      this.observacionesService()
        .update(this.observaciones)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Observaciones is updated with identifier ' + param.id;
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
      this.observacionesService()
        .create(this.observaciones)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Observaciones is created with identifier ' + param.id;
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

  public retrieveObservaciones(observacionesId): void {
    this.observacionesService()
      .find(observacionesId)
      .then(res => {
        this.observaciones = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.proyectoService()
      .retrieve()
      .then(res => {
        this.proyectos = res.data;
      });
  }
}
