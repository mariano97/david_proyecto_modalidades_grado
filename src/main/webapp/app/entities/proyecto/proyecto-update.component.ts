import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import TablaContenidoService from '@/entities/tabla-contenido/tabla-contenido.service';
import { ITablaContenido } from '@/shared/model/tabla-contenido.model';

import EmpresaService from '@/entities/empresa/empresa.service';
import { IEmpresa } from '@/shared/model/empresa.model';

import ArlService from '@/entities/arl/arl.service';
import { IArl } from '@/shared/model/arl.model';

import { IProyecto, Proyecto } from '@/shared/model/proyecto.model';
import ProyectoService from './proyecto.service';

const validations: any = {
  proyecto: {
    nombre: {
      required,
    },
    acta: {
      required,
    },
    fechaInicio: {
      required,
    },
    fechaTermino: {},
    tipoModalidad: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class ProyectoUpdate extends Vue {
  @Inject('proyectoService') private proyectoService: () => ProyectoService;
  @Inject('alertService') private alertService: () => AlertService;

  public proyecto: IProyecto = new Proyecto();

  @Inject('tablaContenidoService') private tablaContenidoService: () => TablaContenidoService;

  public tablaContenidos: ITablaContenido[] = [];

  @Inject('empresaService') private empresaService: () => EmpresaService;

  public empresas: IEmpresa[] = [];

  @Inject('arlService') private arlService: () => ArlService;

  public arls: IArl[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.proyectoId) {
        vm.retrieveProyecto(to.params.proyectoId);
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
    if (this.proyecto.id) {
      this.proyectoService()
        .update(this.proyecto)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Proyecto is updated with identifier ' + param.id;
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
      this.proyectoService()
        .create(this.proyecto)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Proyecto is created with identifier ' + param.id;
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

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_TIME_LONG_FORMAT);
    }
    return null;
  }

  public updateInstantField(field, event) {
    if (event.target.value) {
      this.proyecto[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.proyecto[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.proyecto[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.proyecto[field] = null;
    }
  }

  public retrieveProyecto(proyectoId): void {
    this.proyectoService()
      .find(proyectoId)
      .then(res => {
        res.fechaInicio = new Date(res.fechaInicio);
        res.fechaTermino = new Date(res.fechaTermino);
        this.proyecto = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.tablaContenidoService()
      .retrieve()
      .then(res => {
        this.tablaContenidos = res.data;
      });
    this.empresaService()
      .retrieve()
      .then(res => {
        this.empresas = res.data;
      });
    this.arlService()
      .retrieve()
      .then(res => {
        this.arls = res.data;
      });
  }
}
