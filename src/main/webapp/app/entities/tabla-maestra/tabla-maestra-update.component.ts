import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import { ITablaMaestra, TablaMaestra } from '@/shared/model/tabla-maestra.model';
import TablaMaestraService from './tabla-maestra.service';

const validations: any = {
  tablaMaestra: {
    nombre: {
      required,
    },
    codigo: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class TablaMaestraUpdate extends Vue {
  @Inject('tablaMaestraService') private tablaMaestraService: () => TablaMaestraService;
  @Inject('alertService') private alertService: () => AlertService;

  public tablaMaestra: ITablaMaestra = new TablaMaestra();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.tablaMaestraId) {
        vm.retrieveTablaMaestra(to.params.tablaMaestraId);
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
    if (this.tablaMaestra.id) {
      this.tablaMaestraService()
        .update(this.tablaMaestra)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A TablaMaestra is updated with identifier ' + param.id;
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
      this.tablaMaestraService()
        .create(this.tablaMaestra)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A TablaMaestra is created with identifier ' + param.id;
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

  public retrieveTablaMaestra(tablaMaestraId): void {
    this.tablaMaestraService()
      .find(tablaMaestraId)
      .then(res => {
        this.tablaMaestra = res;
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
