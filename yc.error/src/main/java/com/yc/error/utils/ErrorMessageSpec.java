package com.yc.error.utils;

import org.json.JSONObject;

public class ErrorMessageSpec
{
    public JSONObject getErrorMessage(Class<?> clazz)
    {
        JSONObject error = new JSONObject();
        
        if (clazz == NoContent.class) 
        {
            error.put("code", NoContent.Code.Value);
            error.put("message", NoContent.Message.Pt_Br);
        } 
        else if (clazz == NoContent.Message.Persistence.class) 
        {
            error.put("code", NoContent.Code.Value);
            error.put("message", NoContent.Message.Persistence.Pt_Br);
        } 
        else if (clazz == BadRequest.class) 
        {
            error.put("code", BadRequest.Code.Value);
            error.put("message", BadRequest.Message.Pt_Br);
        } 
        else if (clazz == Unauthorized.class) 
        {
            error.put("code", Unauthorized.Code.Value);
            error.put("message", Unauthorized.Message.Pt_Br);
        } 
        else if (clazz == ForbiddenAccess.class) 
        {
            error.put("code", ForbiddenAccess.Code.Value);
            error.put("message", ForbiddenAccess.Message.Pt_Br);
        } 
        else if (clazz == NotAcceptable.class) 
        {
            error.put("code", NotAcceptable.Code.Value);
            error.put("message", NotAcceptable.Message.Pt_Br);
        } 
        else if (clazz == Conflict.class) 
        {
            error.put("code", Conflict.Code.Value);
            error.put("message", Conflict.Message.Pt_Br);
        } 
        else if (clazz == Conflict.Message.Persistence.class) 
        {
            error.put("code", Conflict.Code.Value);
            error.put("message", Conflict.Message.Persistence.Pt_Br);
        } 
        else if (clazz == PayloadTooLarge.class) 
        {
            error.put("code", PayloadTooLarge.Code.Value);
            error.put("message", PayloadTooLarge.Message.Pt_Br);
        } 
        else if (clazz == PayloadTooLarge.class) 
        {
            error.put("code", PayloadTooLarge.Code.Value);
            error.put("message", PayloadTooLarge.Message.Filer.Pt_Br);
        } 
        else if (clazz == InternalServerError.class) 
        {
            error.put("code", InternalServerError.Code.Value);
            error.put("message", InternalServerError.Message.Pt_Br);
        } 
        else if (clazz == UnexpectedFailed.class) 
        {
            error.put("code", UnexpectedFailed.Code.Value);
            error.put("message", UnexpectedFailed.Message.Pt_Br);
        }
        
        return error;
    }
    
    public interface NoContent
    {
        public interface Code 
        {
            public static String Value = "204";
        }
        
        public interface Message 
        {
            public String Pt_Br = "Recursos não foram localizados.";
            
            public interface Persistence 
            {
                public String Pt_Br = "Não foram localizados registros no banco de dados.";
            }
        }
    }
    
    public interface BadRequest
    {
        public interface Code 
        {
            public static String Value = "400";
        }
        
        public interface Message 
        {
            public String Pt_Br = "Falha na estrutura do recurso enviado na requisição.";
            
            public interface Persistence 
            {
                public String Pt_Br = "Falha na estrutura do JSON enviado na requisição.";
            }
        }
    }
    
    public interface Unauthorized
    {
        public interface Code 
        {
            public static String Value = "401";
        }
        
        public interface Message 
        {
            public String Pt_Br = "As credenciais de acesso ao recurso são insuficientes.";
        }
    }
    
    public interface ForbiddenAccess
    {
        public interface Code 
        {
            public static String Value = "403";
        }
        
        public interface Message 
        {
            public String Pt_Br = "O acesso ao recurso foi negado.";
        }
    }
    
    public interface NotFound
    {
        public interface Code 
        {
            public static String Value = "404";
        }
        
        public interface Message 
        {
            public String Pt_Br = "A URI não existe.";
        }
    }
    
    public interface NotAcceptable
    {
        public interface Code 
        {
            public static String Value = "406";
        }
        
        public interface Message 
        {
            public String Pt_Br = "";
        }
    }
    
    public interface Conflict
    {
        public interface Code 
        {
            public static String Value = "409";
        }
        
        public interface Message 
        {
            public String Pt_Br = "Já existe um registro de mesma identidade.";
            
            public interface Persistence
            {
                public String Pt_Br = "Já existe um registro persistido no banco de dados com a mesma chave única.";
            }
            
            public interface Filer
            {
                public String Pt_Br = "Já existe um arquivo (ou pasta) com a mesma chave de identificação.";
            }
        }
    }
    
    public interface PayloadTooLarge
    {
        public interface Code 
        {
            public static String Value = "413";
        }
        
        public interface Message 
        {
            public String Pt_Br = "O tamanho do recurso enviado é maior do que o autorizado.";
            
            public interface Filer
            {
                public String Pt_Br = "O tamanho do arquivo enviado é maior do que o autorizado.";
            }
        }
    }
    
    public interface InternalServerError
    {
        public interface Code 
        {
            public static String Value = "500";
        }
        
        public interface Message 
        {
            public String Pt_Br = "Falha inesperada no servidor, solicite suporte.";
        }
    }
    
    public interface UnexpectedFailed
    {
        public interface Code 
        {
            public static String Value = "510";
        }
        
        public interface Message 
        {
            public String Pt_Br = "Falha inesperada, solicite suporte.";
        }
    }
}
