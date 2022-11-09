import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import TablaContenidoService from '@/entities/tabla-contenido/tabla-contenido.service';
import { ITablaContenido } from '@/shared/model/tabla-contenido.model';

import ProyectoService from '@/entities/proyecto/proyecto.service';
import { IProyecto } from '@/shared/model/proyecto.model';

import { IEstudiante, Estudiante } from '@/shared/model/estudiante.model';
import EstudianteService from './estudiante.service';

const validations: any = {
  estudiante: {
    primerNombre: {
      required,
    },
    segundoNombre: {},
    apellidos: {
      required,
    },
    numeroDocumento: {
      required,
    },
    codigoEstudiantil: {
      required,
    },
    celular: {
      required,
    },
    email: {
      required,
    },
    tipoDocumento: {
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
export default class EstudianteUpdate extends Vue {
  @Inject('estudianteService') private estudianteService: () => EstudianteService;
  @Inject('alertService') private alertService: () => AlertService;

  public estudiante: IEstudiante = new Estudiante();

  @Inject('tablaContenidoService') private tablaContenidoService: () => TablaContenidoService;

  public tablaContenidos: ITablaContenido[] = [];

  @Inject('proyectoService') private proyectoService: () => ProyectoService;

  public proyectos: IProyecto[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.estudianteId) {
        vm.retrieveEstudiante(to.params.estudianteId);
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
    if (this.estudiante.id) {
      this.estudianteService()
        .update(this.estudiante)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Estudiante is updated with identifier ' + param.id;
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
      this.estudianteService()
        .create(this.estudiante)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Estudiante is created with identifier ' + param.id;
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

  public retrieveEstudiante(estudianteId): void {
    this.estudianteService()
      .find(estudianteId)
      .then(res => {
        this.estudiante = res;
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
    this.proyectoService()
      .retrieve()
      .then(res => {
        this.proyectos = res.data;
      });
  }
}
