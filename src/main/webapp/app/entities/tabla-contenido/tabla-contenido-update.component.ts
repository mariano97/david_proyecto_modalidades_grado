import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import TablaMaestraService from '@/entities/tabla-maestra/tabla-maestra.service';
import { ITablaMaestra } from '@/shared/model/tabla-maestra.model';

import { ITablaContenido, TablaContenido } from '@/shared/model/tabla-contenido.model';
import TablaContenidoService from './tabla-contenido.service';

const validations: any = {
  tablaContenido: {
    nombre: {
      required,
    },
    codigo: {
      required,
    },
    tablaMaestra: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class TablaContenidoUpdate extends Vue {
  @Inject('tablaContenidoService') private tablaContenidoService: () => TablaContenidoService;
  @Inject('alertService') private alertService: () => AlertService;

  public tablaContenido: ITablaContenido = new TablaContenido();

  @Inject('tablaMaestraService') private tablaMaestraService: () => TablaMaestraService;

  public tablaMaestras: ITablaMaestra[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.tablaContenidoId) {
        vm.retrieveTablaContenido(to.params.tablaContenidoId);
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
    if (this.tablaContenido.id) {
      this.tablaContenidoService()
        .update(this.tablaContenido)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A TablaContenido is updated with identifier ' + param.id;
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
      this.tablaContenidoService()
        .create(this.tablaContenido)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A TablaContenido is created with identifier ' + param.id;
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

  public retrieveTablaContenido(tablaContenidoId): void {
    this.tablaContenidoService()
      .find(tablaContenidoId)
      .then(res => {
        this.tablaContenido = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.tablaMaestraService()
      .retrieve()
      .then(res => {
        this.tablaMaestras = res.data;
      });
  }
}
