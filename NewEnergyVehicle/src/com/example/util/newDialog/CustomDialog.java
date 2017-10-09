package com.example.util.newDialog;



import com.example.newenergyvehicle.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomDialog extends Dialog {  
    
    public CustomDialog(Context context) {   
        super(context);  
    }  
  
    public CustomDialog(Context context, int theme) {  
        super(context, theme);  
    }  
   
    public static class Builder {
        private Activity context;  
        private String title;  
        private Object contentView;  
        private TextView contentText;
        private String positiveButtonText;  
        private String negativeButtonText;  
        private LinearLayout contentLine;  
        private DialogInterface.OnClickListener positiveButtonClickListener;  
        private DialogInterface.OnClickListener negativeButtonClickListener;  
        private String centerButtonText;
        private DialogInterface.OnClickListener centerButtonClickListener;  
        private CustomDialog dialog ;
        public Builder(Activity context) {  
            this.context = context;  
        }  
  
        /** 
         * Set the Dialog message from resource 
         *  
         * @param title 
         * @return 
         */  
  
        /** 
         * Set the Dialog title from resource 
         *  
         * @param title 
         * @return 
         */  
        public Builder setTitle(int title) {  
            this.title = (String) context.getText(title);  
            return this;  
        }  
  
        /** 
         * Set the Dialog title from String 
         *  
         * @param title 
         * @return 
         */  
  
        public Builder setTitle(String title) {  
            this.title = title;  
            return this;  
        }  
  
        public Builder setContentView(Object v) {  
        	this.contentView = v;
            return this;  
        }  
        
        public void gone() {
        	if(dialog!=null) {
        		dialog.hide();
        	}
        }
        public void visible() {
        	if(dialog!=null) {
        		dialog.show();
        	}
        }
        
        public void hide() {
        	if(dialog!=null) {
        		dialog.dismiss();
        	}
        	
        }
  
        /** 
         * Set the positive button resource and it's listener 
         *  
         * @param positiveButtonText 
         * @return 
         */  
        public Builder setPositiveButton(int positiveButtonText,  
                DialogInterface.OnClickListener listener) {  
            this.positiveButtonText = (String) context  
                    .getText(positiveButtonText);  
            this.positiveButtonClickListener = listener;  
            return this;  
        } 
        
       
        
        public Builder setCenterButton(int positiveButtonText,  
                DialogInterface.OnClickListener listener) {  
            this.centerButtonText = (String) context  
                    .getText(positiveButtonText);  
            this.centerButtonClickListener = listener;  
            return this;  
        } 
        public Builder setCenterButton(String positiveButtonText,  
                DialogInterface.OnClickListener listener) {  
        	 this.centerButtonText = positiveButtonText;  
            this.centerButtonClickListener = listener;  
            return this;  
        } 
        public Builder setPositiveButton(String positiveButtonText,  
                DialogInterface.OnClickListener listener) {  
            this.positiveButtonText = positiveButtonText;  
            this.positiveButtonClickListener = listener;  
            return this;  
        }  
  
        public Builder setNegativeButton(int negativeButtonText,  
                DialogInterface.OnClickListener listener) {  
            this.negativeButtonText = (String) context  
                    .getText(negativeButtonText);  
            this.negativeButtonClickListener = listener;  
            return this;  
        }  
  
        public Builder setNegativeButton(String negativeButtonText,  
                DialogInterface.OnClickListener listener) {  
            this.negativeButtonText = negativeButtonText;  
            this.negativeButtonClickListener = listener;  
            return this;  
        }  
  
        public CustomDialog create(boolean isMark) {  
            LayoutInflater inflater = (LayoutInflater) context  
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
            // instantiate the dialog with the custom Theme  
            dialog = new CustomDialog(context,R.style.Dialog);
//            dialog.setCancelable(false);
            View layout = inflater.inflate(R.layout.dialog_normal_layout, null);  
            dialog.addContentView(layout, new LayoutParams(  
                    LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));  
            if(isMark) {
            	layout.findViewById(R.id.titleDialogLine).setVisibility(View.GONE);
            	layout.findViewById(R.id.buttonDialogLine).setVisibility(View.GONE);
            	if(contentView!=null) {
                	if(contentView instanceof View) {
                		contentLine.removeAllViews();
                		View  contentViewNew = (View) contentView;
                		LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                		contentViewNew.setLayoutParams(ll);
                    	contentLine.addView(contentViewNew);
                	}
                	else if(contentView instanceof String) {
                		contentText =  ((TextView) layout.findViewById(R.id.contentText))  ;
                    	contentText.setText(contentView.toString());
                	}
                }
            	
            }
            else {
            	 // set the dialog title  
            	if(title!=null) {
            		 ((TextView) layout.findViewById(R.id.titleDialog)).setText(title);
            	}
            	else {
            		layout.findViewById(R.id.titleDialogLine).setVisibility(View.GONE);
            	}
                final LinearLayout poBu =  ((LinearLayout) layout.findViewById(R.id.negativeButton)) ;
                final LinearLayout neBu = ((LinearLayout) layout.findViewById(R.id.positiveButton))  ;
                final LinearLayout ceBu = ((LinearLayout) layout.findViewById(R.id.centerButton))  ;
                contentLine = ((LinearLayout) layout.findViewById(R.id.content))  ;
                DisplayMetrics metric = new DisplayMetrics();
                context.getWindowManager().getDefaultDisplay().getMetrics(metric);
                if(contentView!=null) {
                	if(contentView instanceof View) {
                		contentLine.removeAllViews();
                		View  contentViewNew = (View) contentView;
                		LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                		contentViewNew.setLayoutParams(ll);
                    	contentLine.addView(contentViewNew);
                	}
                	else if(contentView instanceof String) {
                		contentText =  ((TextView) layout.findViewById(R.id.contentText))  ;
                    	contentText.setText(contentView.toString());
                	}
                }
                if(positiveButtonText == null && negativeButtonText==null && centerButtonText ==null) {
                	layout.findViewById(R.id.buttonDialogLine).setVisibility(View.GONE);
                }
                else {
                	 // set the confirm button  
                    if (positiveButtonText != null) {  
                    	((TextView)layout.findViewById(R.id.negativeButtonText)).setText(positiveButtonText);  
                        if (positiveButtonClickListener != null) {
                        	poBu.setOnClickListener(new View.OnClickListener() {  
                                        public void onClick(View v) {
                                        	poBu.setBackgroundColor(0xfff5f5f5);
                                            positiveButtonClickListener.onClick(dialog,  
                                                    DialogInterface.BUTTON_POSITIVE);
                                            if(contentView instanceof String) 
                                            	dialog.hide();  
                                        }  
                                    });  
                        } 
                        else {
                        	poBu.setOnClickListener(new View.OnClickListener() {  
                                public void onClick(View v) {  
                                	poBu.setBackgroundColor(0xfff5f5f5);
                                	dialog.hide();  
                                }  
                            }); 
                        }
                    } else {  
                        // if no confirm button just set the visibility to GONE  
                    	poBu.setVisibility(  
                                View.GONE);  
                    }  
                    
                 
                    // set the cancel button  
                    if (negativeButtonText != null) {  
                    	((TextView)layout.findViewById(R.id.positiveButtonText)).setText(negativeButtonText);  
                        if (negativeButtonClickListener != null) {  
                        	neBu.setOnClickListener(new View.OnClickListener() {  
                                        public void onClick(View v) {  
                                        	neBu.setBackgroundColor(0xfff5f5f5);
                                            negativeButtonClickListener.onClick(dialog,  
                                                    DialogInterface.BUTTON_NEGATIVE);  
                                            if(contentView instanceof String) 
                                            dialog.hide();  
                                        }  
                                    });  
                        }
                        else {
                        	neBu.setOnClickListener(new View.OnClickListener() {  
                                public void onClick(View v) { 
                                	neBu.setBackgroundColor(0xfff5f5f5);
                                	dialog.hide();  
                                }  
                            });
                        }
                    } else {  
                        // if no confirm button just set the visibility to GONE  
                    	neBu.setVisibility(  
                                View.GONE);  
                    }  
                    
                 // set the confirm button  
                    if (centerButtonText != null) {  
                    	((TextView)layout.findViewById(R.id.centerButtonText)).setText(centerButtonText);  
                        if (centerButtonClickListener != null) {
                        	ceBu.setOnClickListener(new View.OnClickListener() {  
                                        public void onClick(View v) {
                                        	ceBu.setBackgroundColor(0xfff5f5f5);
                                        	centerButtonClickListener.onClick(dialog,  
                                                    DialogInterface.BUTTON_POSITIVE);
                                        	if(contentView instanceof String) 
                                        	dialog.hide();  
                                        }  
                                    });  
                        } 
                        else {
                        	ceBu.setOnClickListener(new View.OnClickListener() {  
                                public void onClick(View v) {  
                                	ceBu.setBackgroundColor(0xfff5f5f5);
                                	dialog.hide();  
                                }  
                            }); 
                        }
                    } else {  
                        // if no confirm button just set the visibility to GONE  
                    	ceBu.setVisibility(  
                                View.GONE);
                    	if(negativeButtonText!=null) {
                    		poBu.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 6f));
                        	neBu.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 6f));
                    	}
                    	else {
                    		poBu.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 12f));
                    	}
                    }
                }
                
            }
            dialog.setContentView(layout); 
             
            return dialog;  
        }  
  
    }  
}  