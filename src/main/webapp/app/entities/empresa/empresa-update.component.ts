import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import { IEmpresa, Empresa } from '@/shared/model/empresa.model';
import EmpresaService from './empresa.service';

const validations: any = {
  empresa: {
    nombre: {
      required,
    },
    nit: {
      required,
    },
    telefono: {},
    email: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class EmpresaUpdate extends Vue {
  @Inject('empresaService') private empresaService: () => EmpresaService;
  @Inject('alertService') private alertService: () => AlertService;

  public empresa: IEmpresa = new Empresa();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.empresaId) {
        vm.retrieveEmpresa(to.params.empresaId);
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
    if (this.empresa.id) {
      this.empresaService()
        .update(this.empresa)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Empresa is updated with identifier ' + param.id;
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
      this.empresaService()
        .create(this.empresa)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Empresa is created with identifier ' + param.id;
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

  public retrieveEmpresa(empresaId): void {
    this.empresaService()
      .find(empresaId)
      .then(res => {
        this.empresa = res;
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
